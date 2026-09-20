package org.stocktrace.repository;

import org.stocktrace.config.ConexaoBanco;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Movimentacao;
import org.stocktrace.model.TipoMovimentacao;

import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MovimentacaoRepository {

    private final EstoqueRepository estoqueRepository =
            new EstoqueRepository();


    // Cadastra uma nova movimentação no banco
    // e retorna a movimentação com o ID gerado.
    public Movimentacao cadastrarMovimentacao(Movimentacao movimentacao)
            throws SQLException, IOException {

        String sql = """
                INSERT INTO movimentacao
                (estoque_id, tipo, quantidade, data_hora, motivo, observacao, responsavel)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();

                PreparedStatement stmt = conexao.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            stmt.setLong(
                    1,
                    movimentacao.getEstoque().getId()
            );

            // Converte o enum ENTRADA ou SAIDA para texto.
            stmt.setString(
                    2,
                    movimentacao.getTipo().name()
            );

            stmt.setInt(
                    3,
                    movimentacao.getQuantidade()
            );

            // Converte LocalDateTime para Timestamp do banco.
            stmt.setTimestamp(
                    4,
                    Timestamp.valueOf(
                            movimentacao.getDataHora()
                    )
            );

            stmt.setString(
                    5,
                    movimentacao.getMotivo()
            );

            stmt.setString(
                    6,
                    movimentacao.getObservacao()
            );

            stmt.setString(
                    7,
                    movimentacao.getResponsavel()
            );

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {

                    return new Movimentacao(
                            rs.getLong(1),
                            movimentacao.getEstoque(),
                            movimentacao.getTipo(),
                            movimentacao.getQuantidade(),
                            movimentacao.getDataHora(),
                            movimentacao.getMotivo(),
                            movimentacao.getObservacao(),
                            movimentacao.getResponsavel()
                    );
                }
            }

            throw new SQLException(
                    "Não foi possível obter o ID da movimentação cadastrada."
            );
        }
    }


    // Busca uma movimentação pelo ID.
    public Movimentacao buscarMovimentacaoPorId(Long id)
            throws SQLException, IOException {

        String sql = """
                SELECT id,
                       estoque_id,
                       tipo,
                       quantidade,
                       data_hora,
                       motivo,
                       observacao,
                       responsavel
                FROM movimentacao
                WHERE id = ?
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapearMovimentacao(rs);
                }
            }
        }

        throw new SQLException(
                "Movimentação não encontrada. ID: " + id
        );
    }


    // Lista todas as movimentações cadastradas.
    public List<Movimentacao> listarMovimentacoes()
            throws SQLException, IOException {

        String sql = """
                SELECT id,
                       estoque_id,
                       tipo,
                       quantidade,
                       data_hora,
                       motivo,
                       observacao,
                       responsavel
                FROM movimentacao
                ORDER BY data_hora DESC
                """;

        List<Movimentacao> movimentacoes =
                new ArrayList<>();

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                movimentacoes.add(
                        mapearMovimentacao(rs)
                );
            }
        }

        return movimentacoes;
    }


    /*
     * Lista todas as movimentações de um estoque específico.
     *
     * Como cada estoque representa Produto + Loja,
     * conseguimos consultar o histórico daquele produto
     * naquela loja.
     */
    public List<Movimentacao> listarMovimentacoesPorEstoque(
            Long estoqueId
    ) throws SQLException, IOException {

        String sql = """
                SELECT id,
                       estoque_id,
                       tipo,
                       quantidade,
                       data_hora,
                       motivo,
                       observacao,
                       responsavel
                FROM movimentacao
                WHERE estoque_id = ?
                ORDER BY data_hora DESC
                """;

        List<Movimentacao> movimentacoes =
                new ArrayList<>();

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setLong(1, estoqueId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    movimentacoes.add(
                            mapearMovimentacao(rs)
                    );
                }
            }
        }

        return movimentacoes;
    }


    /*
     * Transforma uma linha da tabela movimentacao
     * em um objeto Movimentacao.
     *
     * A tabela guarda apenas o estoque_id.
     * Então buscamos o objeto Estoque completo
     * usando o EstoqueRepository.
     */
    private Movimentacao mapearMovimentacao(ResultSet rs)
            throws SQLException, IOException {

        Long estoqueId =
                rs.getLong("estoque_id");

        Estoque estoque =
                estoqueRepository.buscarEstoquePorId(
                        estoqueId
                );


        // Converte o texto ENTRADA ou SAIDA
        // do banco para o enum TipoMovimentacao.
        TipoMovimentacao tipo =
                TipoMovimentacao.valueOf(
                        rs.getString("tipo")
                );


        // Converte o Timestamp do banco
        // novamente para LocalDateTime.
        LocalDateTime dataHora =
                rs.getTimestamp("data_hora")
                        .toLocalDateTime();


        return new Movimentacao(
                rs.getLong("id"),
                estoque,
                tipo,
                rs.getInt("quantidade"),
                dataHora,
                rs.getString("motivo"),
                rs.getString("observacao"),
                rs.getString("responsavel")
        );
    }
}