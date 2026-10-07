package org.stocktrace.repository;

import org.springframework.stereotype.Repository;
import org.stocktrace.config.ConexaoBanco;
import org.stocktrace.exception.EntidadeNaoEncontradaException;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Loja;
import org.stocktrace.model.Produto;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class EstoqueRepository {

    private final ProdutoRepository produtoRepository;
    private final LojaRepository lojaRepository;

    public EstoqueRepository(
            ProdutoRepository produtoRepository,
            LojaRepository lojaRepository
    ) {
        this.produtoRepository = produtoRepository;
        this.lojaRepository = lojaRepository;
    }


    // Cadastra o estoque de um produto em uma loja
    // e retorna o estoque com o ID gerado pelo banco.
    public Estoque cadastrarEstoque(Estoque estoque)
            throws SQLException, IOException {

        String sql = """
                INSERT INTO estoque (produto_id, loja_id, quantidade_atual)
                VALUES (?, ?, ?)
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            stmt.setLong(1, estoque.getProduto().getId());
            stmt.setLong(2, estoque.getLoja().getId());
            stmt.setInt(3, estoque.getQuantidadeAtual());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {

                    return new Estoque(
                            rs.getLong(1),
                            estoque.getProduto(),
                            estoque.getLoja(),
                            estoque.getQuantidadeAtual()
                    );
                }
            }

            throw new SQLException(
                    "Não foi possível obter o ID do estoque cadastrado."
            );
        }
    }


    // Busca um estoque pelo ID.
    public Estoque buscarEstoquePorId(Long id)
            throws SQLException, IOException {

        String sql = """
                SELECT id, produto_id, loja_id, quantidade_atual
                FROM estoque
                WHERE id = ?
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapearEstoque(rs);
                }
            }
        }

        throw new EntidadeNaoEncontradaException(
                "Estoque não encontrado. ID: " + id
        );
    }

    public Estoque buscarEstoquePorIdParaAtualizacao(
            Connection conexao,
            Long id
    ) throws SQLException, IOException {

        String sql = """
            SELECT id, produto_id, loja_id, quantidade_atual
            FROM estoque
            WHERE id = ?
            FOR UPDATE
            """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapearEstoque(rs);
                }
            }
        }

        throw new EntidadeNaoEncontradaException(
                "Estoque não encontrado. ID: " + id
        );
    }



    /*
     * Busca o estoque de um determinado produto
     * em uma determinada loja.
     *
     * Exemplo:
     * Produto = Arroz
     * Loja = Centro
     */
    public Estoque buscarEstoquePorProdutoELoja(
            Long produtoId,
            Long lojaId
    ) throws SQLException, IOException {

        String sql = """
                SELECT id, produto_id, loja_id, quantidade_atual
                FROM estoque
                WHERE produto_id = ?
                AND loja_id = ?
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setLong(1, produtoId);
            stmt.setLong(2, lojaId);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapearEstoque(rs);
                }
            }
        }

        // Se ainda não existir estoque para esse produto nessa loja,
        // retorna null.
        return null;
    }


    // Lista todos os estoques cadastrados.
    public List<Estoque> listarEstoques()
            throws SQLException, IOException {

        String sql = """
                SELECT id, produto_id, loja_id, quantidade_atual
                FROM estoque
                ORDER BY loja_id, produto_id
                """;

        List<Estoque> estoques = new ArrayList<>();

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                estoques.add(mapearEstoque(rs));
            }
        }

        return estoques;
    }


    // Atualiza somente a quantidade atual do estoque.
    public void atualizarQuantidade(Connection conexao, Estoque estoque)
            throws SQLException {

        String sql = """
            UPDATE estoque
            SET quantidade_atual = ?
            WHERE id = ?
            """;

        try (PreparedStatement stmt = conexao.prepareStatement(sql)) {
            stmt.setInt(1, estoque.getQuantidadeAtual());
            stmt.setLong(2, estoque.getId());

            int linhasAlteradas = stmt.executeUpdate();

            // Zero pode significar que a quantidade já tinha esse valor.
            // Por isso, verificamos se o estoque realmente existe.
            if (linhasAlteradas == 0) {
                String consulta = "SELECT id FROM estoque WHERE id = ?";

                try (PreparedStatement busca = conexao.prepareStatement(consulta)) {
                    busca.setLong(1, estoque.getId());

                    try (ResultSet rs = busca.executeQuery()) {
                        if (!rs.next()) {
                            throw new EntidadeNaoEncontradaException(
                                    "Estoque não encontrado. ID: " + estoque.getId()
                            );
                        }
                    }
                }
            }
        }
    }


    /*
     * Transforma uma linha da tabela estoque
     * em um objeto Estoque.
     * montando o objeto Estoque completo.
     */
    private Estoque mapearEstoque(ResultSet rs)
            throws SQLException, IOException {

        Long produtoId = rs.getLong("produto_id");
        Long lojaId = rs.getLong("loja_id");

        Produto produto =
                produtoRepository.buscarProdutoPorId(produtoId);

        Loja loja =
                lojaRepository.buscarLojaPorId(lojaId);

        return new Estoque(
                rs.getLong("id"),
                produto,
                loja,
                rs.getInt("quantidade_atual")
        );
    }
}