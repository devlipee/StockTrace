package org.stocktrace.repository;

import org.stocktrace.config.ConexaoBanco;
import org.stocktrace.model.Categoria;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CategoriaRepository {

    public Categoria cadastrarCategoria(Categoria categoria)
            throws SQLException, IOException {

        String sql = """
                INSERT INTO categoria (nome, descricao, ativo)
                VALUES (?, ?, ?)
                """;

        try (
                Connection conexao = ConexaoBanco.conectar();

                PreparedStatement stmt = conexao.prepareStatement(
                        sql,
                        Statement.RETURN_GENERATED_KEYS
                )
        ) {

            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getDescricao());
            stmt.setBoolean(3, categoria.isAtivo());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {

                    Long idGerado = rs.getLong(1);

                    return new Categoria(
                            idGerado,
                            categoria.getNome(),
                            categoria.getDescricao(),
                            categoria.isAtivo()
                    );
                }
            }

            throw new SQLException(
                    "Não foi possível obter o ID da categoria cadastrada."
            );
        }
    }
}