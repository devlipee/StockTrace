package org.stocktrace.repository;

import org.stocktrace.config.ConexaoBanco;
import org.stocktrace.model.Loja;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LojaRepository {

    // Cadastra uma nova loja no banco e retorna a loja com o ID gerado.
    public Loja cadastrarLoja(Loja loja) throws SQLException, IOException {
        String sql = """
                INSERT INTO loja (nome, cidade, bairro, rua, numero, complemento, cep)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)
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

            throw new SQLException("Não foi possível obter o ID da loja cadastrada.");
        }
    }

    /*
     * o metodo buscar lojas depende de mapearlojas que transformas as linhas da tabela em um objeto loja
     *
     * 1. Faz um SELECT na tabela loja.
     * 2. O banco devolve os dados em um ResultSet.
     * 3. O while percorre cada linha retornada pelo banco.
     * 4. Para cada linha, o metodo mapearLoja() transforma os dados em um objeto Loja.
     * 5. Cada objeto Loja é adicionado na lista.
     * 6. No final, o metodo retorna a lista com todas as lojas encontradas.
     */
    public List<Loja> listarLojas() throws SQLException, IOException {
        String sql = """
                SELECT id, nome, cidade, bairro, rua, numero, complemento, cep
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

    // Atualiza os dados de uma loja já existente usando o ID da loja.
    public void atualizarLoja(Loja loja) throws SQLException, IOException {
        String sql = """
                UPDATE loja
                SET nome = ?, cidade = ?, bairro = ?, rua = ?, numero = ?, complemento = ?, cep = ?
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
                throw new SQLException("Loja não encontrada. ID: " + loja.getId());
            }
        }
    }

    // Exclui uma loja do banco pelo ID informado.
    public void deletarLoja(Long id) throws SQLException, IOException {
        String sql = "DELETE FROM loja WHERE id = ?";

        try (
                Connection conexao = ConexaoBanco.conectar();
                PreparedStatement stmt = conexao.prepareStatement(sql)
        ) {
            stmt.setLong(1, id);

            int linhasDeletadas = stmt.executeUpdate();

            if (linhasDeletadas == 0) {
                throw new SQLException("Loja não encontrada. ID: " + id);
            }
        }
    }

    // Converte uma linha retornada pelo banco em um objeto Loja.
    //Toda vez que um metodo fizer um SELECT de loja, usamos este metodo para transformar a linha do banco, em um objeto Loja
    private Loja mapearLoja(ResultSet rs) throws SQLException {
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