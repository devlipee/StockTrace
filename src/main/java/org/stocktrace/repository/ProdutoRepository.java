package org.stocktrace.repository;

import org.stocktrace.config.ConexaoBanco;
import org.stocktrace.model.CategoriaProduto;
import org.stocktrace.model.Produto;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoRepository {

    // Cadastra um novo produto no banco e retorna o produto com o ID gerado.
    public Produto cadastrarProduto(Produto produto) throws SQLException, IOException {

        String sql = """
                INSERT INTO produto
                (codigo, nome, descricao, preco, unidade_medida, ativo, categoria)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getDescricao());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setString(5, produto.getUnidadeMedida());
            stmt.setBoolean(6, produto.isAtivo());

            // Transforma o enum em texto para salvar no banco.
            stmt.setString(7, produto.getCategoria().name());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {

                    return new Produto(
                            rs.getLong(1),
                            produto.getCodigo(),
                            produto.getNome(),
                            produto.getDescricao(),
                            produto.getPreco(),
                            produto.getUnidadeMedida(),
                            produto.isAtivo(),
                            produto.getCategoria()
                    );
                }
            }

            throw new SQLException(
                    "Não foi possível obter o ID do produto cadastrado."
            );
        }
    }

    /*
     * Busca todos os produtos cadastrados no banco.
     *
     * O ResultSet percorre as linhas retornadas pelo SELECT
     * e mapearProduto() transforma cada linha em um objeto Produto.
     */
    public List<Produto> listarProdutos() throws SQLException, IOException {

        String sql = """
                SELECT id, codigo, nome, descricao, preco,
                       unidade_medida, ativo, categoria
                FROM produto
                ORDER BY nome
                """;

        List<Produto> produtos = new ArrayList<>();

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {
                produtos.add(mapearProduto(rs));
            }
        }

        return produtos;
    }

    // Busca um produto pelo ID.
    public Produto buscarProdutoPorId(Long id) throws SQLException, IOException {

        String sql = """
                SELECT id, codigo, nome, descricao, preco,
                       unidade_medida, ativo, categoria
                FROM produto
                WHERE id = ?
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }

        throw new SQLException("Produto não encontrado. ID: " + id);
    }

    // Busca um produto pelo código informado.
    public Produto buscarProdutoPorCodigo(String codigo)
            throws SQLException, IOException {

        String sql = """
                SELECT id, codigo, nome, descricao, preco,
                       unidade_medida, ativo, categoria
                FROM produto
                WHERE codigo = ?
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setString(1, codigo);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapearProduto(rs);
                }
            }
        }

        throw new SQLException(
                "Produto não encontrado. Código: " + codigo
        );
    }

    // Atualiza os dados de um produto já existente.
    public void atualizarProduto(Produto produto)
            throws SQLException, IOException {

        String sql = """
                UPDATE produto
                SET codigo = ?,
                    nome = ?,
                    descricao = ?,
                    preco = ?,
                    unidade_medida = ?,
                    ativo = ?,
                    categoria = ?
                WHERE id = ?
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setString(1, produto.getCodigo());
            stmt.setString(2, produto.getNome());
            stmt.setString(3, produto.getDescricao());
            stmt.setBigDecimal(4, produto.getPreco());
            stmt.setString(5, produto.getUnidadeMedida());
            stmt.setBoolean(6, produto.isAtivo());
            stmt.setString(7, produto.getCategoria().name());
            stmt.setLong(8, produto.getId());

            int linhasAlteradas = stmt.executeUpdate();

            if (linhasAlteradas == 0) {
                throw new SQLException(
                        "Produto não encontrado. ID: " + produto.getId()
                );
            }
        }
    }

    // Exclui um produto do banco pelo ID.
    public void deletarProduto(Long id) throws SQLException, IOException {

        String sql = "DELETE FROM produto WHERE id = ?";

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {

            stmt.setLong(1, id);

            int linhasDeletadas = stmt.executeUpdate();

            if (linhasDeletadas == 0) {
                throw new SQLException(
                        "Produto não encontrado. ID: " + id
                );
            }
        }
    }

    /*
     * Transforma uma linha retornada pelo banco em um objeto Produto.
     *
     * A categoria está salva no banco como texto.
     * valueOf() transforma esse texto novamente em CategoriaProduto.
     */
    private Produto mapearProduto(ResultSet rs) throws SQLException {

        CategoriaProduto categoria =
                CategoriaProduto.valueOf(
                        rs.getString("categoria")
                );

        return new Produto(
                rs.getLong("id"),
                rs.getString("codigo"),
                rs.getString("nome"),
                rs.getString("descricao"),
                rs.getBigDecimal("preco"),
                rs.getString("unidade_medida"),
                rs.getBoolean("ativo"),
                categoria
        );
    }
}