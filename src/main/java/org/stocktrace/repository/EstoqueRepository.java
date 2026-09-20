package org.stocktrace.repository;

import org.stocktrace.config.ConexaoBanco;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Loja;
import org.stocktrace.model.Produto;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EstoqueRepository {

    private final ProdutoRepository produtoRepository = new ProdutoRepository();
    private final LojaRepository lojaRepository = new LojaRepository();


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

        throw new SQLException(
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
    public void atualizarQuantidade(Estoque estoque)
            throws SQLException, IOException {

        String sql = """
                UPDATE estoque
                SET quantidade_atual = ?
                WHERE id = ?
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setInt(1, estoque.getQuantidadeAtual());
            stmt.setLong(2, estoque.getId());

            int linhasAlteradas = stmt.executeUpdate();

            if (linhasAlteradas == 0) {
                throw new SQLException(
                        "Estoque não encontrado. ID: " + estoque.getId()
                );
            }
        }
    }


    /*
     * Transforma uma linha da tabela estoque
     * em um objeto Estoque.
     *
     * A tabela estoque guarda:
     *
     * produto_id
     * loja_id
     * quantidade_atual
     *
     * Então buscamos o Produto e a Loja pelos seus IDs
     * e depois montamos o objeto Estoque completo.
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