package org.stocktrace;

import org.stocktrace.config.ConexaoBanco;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

public class Main {

    public static void main(String[] args) {

        try {
            Connection conexao = ConexaoBanco.conectar();

            System.out.println("Conectado com sucesso ao StockTrace!");

            conexao.close();

        } catch (SQLException | IOException e) {
            System.out.println("Erro ao conectar: " + e.getMessage());
        }
    }
}