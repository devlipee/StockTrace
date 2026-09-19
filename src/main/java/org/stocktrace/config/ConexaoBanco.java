package org.stocktrace.config;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class ConexaoBanco {

    public static Connection conectar() throws SQLException, IOException {

        Properties props = new Properties();

        try (InputStream input =
                     ConexaoBanco.class
                             .getClassLoader()
                             .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new IOException(
                        "Arquivo config.properties não encontrado em resources."
                );
            }

            props.load(input);
        }

        String url = props.getProperty("db.url");
        String usuario = props.getProperty("db.user");
        String senha = props.getProperty("db.password");

        return DriverManager.getConnection(
                url,
                usuario,
                senha
        );
    }
}