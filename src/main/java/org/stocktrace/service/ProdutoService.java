package org.stocktrace.service;

import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.repository.ProdutoRepository;

public class ProdutoService {

    private final ProdutoRepository produtoRepository = new ProdutoRepository();

    // Validações privadas reutilizadas pelos métodos deste Service.

    private void validarNome(String nome) {

        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException(
                    "O nome da loja é obrigatório."
            );
        }
    }

    private void validarPreco(Double preco) {
        if (preco == null) {
            throw new IllegalArgumentException("Preço não pode ser nulo");
        }
        if (preco <= 0) {
            throw new IllegalArgumentException("Preço deve ser maior que zero");
        }
    }
}
