package org.stocktrace;

import org.stocktrace.model.Categoria;
import org.stocktrace.repository.CategoriaRepository;

import java.io.IOException;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {

            Categoria categoria = new Categoria(
                    "Bebidas",
                    "Produtos líquidos"
            );

            CategoriaRepository repository =
                    new CategoriaRepository();

            Categoria categoriaCadastrada =
                    repository.cadastrarCategoria(categoria);

            System.out.println(
                    "Categoria cadastrada com sucesso!"
            );

            System.out.println(
                    "ID gerado: " + categoriaCadastrada.getId()
            );

            System.out.println(
                    "Nome: " + categoriaCadastrada.getNome()
            );

        } catch (SQLException | IOException e) {

            System.out.println(
                    "Erro ao cadastrar categoria: " + e.getMessage()
            );
        }
    }
}