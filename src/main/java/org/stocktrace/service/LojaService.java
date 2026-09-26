package org.stocktrace.service;

import org.stocktrace.exception.IdInvalidoException;
import org.stocktrace.exception.NomeLojaInvalidoException;
import org.stocktrace.model.Loja;
import org.stocktrace.repository.LojaRepository;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

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

        validarNome(nome);

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

    public List<Loja> listarLojas()
            throws SQLException, IOException {

        return lojaRepository.listarLojas();
    }

    public Loja buscarLojaPorId(Long id)
            throws SQLException, IOException {

        validarId(id);

        return lojaRepository.buscarLojaPorId(id);
    }

    public Loja atualizarLoja(
            Long id,
            String nome,
            String cidade,
            String bairro,
            String rua,
            String numero,
            String complemento,
            String cep
    ) throws SQLException, IOException {

        validarNome(nome);

        Loja loja = buscarLojaPorId(id);

        loja.atualizarDados(
                nome,
                cidade,
                bairro,
                rua,
                numero,
                complemento,
                cep
        );

        lojaRepository.atualizarLoja(loja);

        return loja;
    }

    public void deletarLoja(Long id)
            throws SQLException, IOException {

        validarId(id);

        lojaRepository.deletarLoja(id);
    }

    // Validações reutilizadas pelos métodos deste Service.

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new NomeLojaInvalidoException(
                    "O nome da loja é obrigatório."
            );
        }
    }

    private void validarId(Long id) {

        if (id == null || id <= 0) {
            throw new IdInvalidoException(
                    "O ID da loja deve ser maior que zero."
            );
        }
    }
}