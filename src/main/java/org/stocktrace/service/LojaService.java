package org.stocktrace.service;

import org.stocktrace.model.Loja;
import org.stocktrace.repository.LojaRepository;

import java.io.IOException;
import java.sql.SQLException;

public class LojaService {

    private final LojaRepository lojaRepository = new LojaRepository();

    public Loja cadastrarLoja(
            String nome,
            String cidade,
            String bairro,
            String rua,
            String numero,
            String complemento,
            String cep
    ) throws SQLException, IOException {

        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException(
                    "O nome da loja é obrigatório."
            );
        }

        Loja loja = new Loja(
                nome,
                cidade,
                bairro,
                rua,
                numero,
                complemento,
                cep
        );

        return lojaRepository.cadastrarLoja(loja);
    }
}