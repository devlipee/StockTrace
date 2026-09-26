package org.stocktrace.repository;

import org.stocktrace.config.ConexaoBanco;
import org.stocktrace.model.Loja;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.stocktrace.exception.LojaNaoEncontradaException;

public class LojaRepository {


    // Cadastra uma nova loja no banco e retorna a loja com o ID gerado.
    public Loja cadastrarLoja(Loja loja)
            throws SQLException, IOException {

        String sql = """
                INSERT INTO loja
                (nome, cidade, bairro, rua, numero, complemento, cep)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();

                PreparedStatement stmt = conexao.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            stmt.setString(1, loja.getNome());
            stmt.setString(2, loja.getCidade());
            stmt.setString(3, loja.getBairro());
            stmt.setString(4, loja.getRua());
            stmt.setString(5, loja.getNumero());
            stmt.setString(6, loja.getComplemento());
            stmt.setString(7, loja.getCep());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {

                    return new Loja(
                            rs.getLong(1),
                            loja.getNome(),
                            loja.getCidade(),
                            loja.getBairro(),
                            loja.getRua(),
                            loja.getNumero(),
                            loja.getComplemento(),
                            loja.getCep()
                    );
                }
            }

            throw new SQLException(
                    "Não foi possível obter o ID da loja cadastrada."
            );
        }
    }


    // Lista todas as lojas cadastradas no banco.
    public List<Loja> listarLojas()
            throws SQLException, IOException {

        String sql = """
                SELECT id, nome, cidade, bairro,
                       rua, numero, complemento, cep
                FROM loja
                ORDER BY nome
                """;

        List<Loja> lojas = new ArrayList<>();

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                lojas.add(mapearLoja(rs));
            }
        }

        return lojas;
    }


    // Busca uma loja específica pelo ID.
    public Loja buscarLojaPorId(Long id)
            throws SQLException, IOException {

        String sql = """
                SELECT id, nome, cidade, bairro,
                       rua, numero, complemento, cep
                FROM loja
                WHERE id = ?
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapearLoja(rs);
                }
            }
        }

        throw new LojaNaoEncontradaException(id);
    }


    /*
     * Atualiza no banco os dados que já estão
     * dentro do objeto Loja.
     */
    public void atualizarLoja(Loja loja)
            throws SQLException, IOException {

        String sql = """
                UPDATE loja
                SET nome = ?,
                    cidade = ?,
                    bairro = ?,
                    rua = ?,
                    numero = ?,
                    complemento = ?,
                    cep = ?
                WHERE id = ?
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setString(1, loja.getNome());
            stmt.setString(2, loja.getCidade());
            stmt.setString(3, loja.getBairro());
            stmt.setString(4, loja.getRua());
            stmt.setString(5, loja.getNumero());
            stmt.setString(6, loja.getComplemento());
            stmt.setString(7, loja.getCep());
            stmt.setLong(8, loja.getId());

            int linhasAlteradas = stmt.executeUpdate();

            if (linhasAlteradas == 0) {
                buscarLojaPorId(loja.getId());
            }

        }
    }


    // Exclui uma loja do banco pelo ID informado.
    public void deletarLoja(Long id)
            throws SQLException, IOException {

        String sql = """
                DELETE FROM loja
                WHERE id = ?
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);

            int linhasDeletadas = stmt.executeUpdate();

            if (linhasDeletadas == 0) {
                throw new LojaNaoEncontradaException(id);
            }
        }
    }


    /*
     * Transforma uma linha retornada pelo banco
     * em um objeto Loja.
     */
    private Loja mapearLoja(ResultSet rs)
            throws SQLException {

        return new Loja(
                rs.getLong("id"),
                rs.getString("nome"),
                rs.getString("cidade"),
                rs.getString("bairro"),
                rs.getString("rua"),
                rs.getString("numero"),
                rs.getString("complemento"),
                rs.getString("cep")
        );
    }
}