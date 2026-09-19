package org.stocktrace.model;

import org.stocktrace.exception.EstoqueInsuficienteException;
import org.stocktrace.exception.QuantidadeInvalidaException;

public class Estoque {

    private Long id;
    private final Produto produto;
    private final Loja loja;
    private int quantidadeAtual;

    // Estoque novo
    public Estoque(Produto produto, Loja loja) {
        this.produto = produto;
        this.loja = loja;
        this.quantidadeAtual = 0;
    }

    // Estoque que já veio do banco
    public Estoque(
            Long id,
            Produto produto,
            Loja loja,
            int quantidadeAtual
    ) {
        this.id = id;
        this.produto = produto;
        this.loja = loja;
        this.quantidadeAtual = quantidadeAtual;
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public Loja getLoja() {
        return loja;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void adicionar(int quantidade) {

        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException(
                    "A quantidade adicionada deve ser maior que zero."
            );
        }

        this.quantidadeAtual += quantidade;
    }

    public void retirar(int quantidade) {

        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException(
                    "A quantidade retirada deve ser maior que zero."
            );
        }

        if (quantidade > this.quantidadeAtual) {
            throw new EstoqueInsuficienteException(
                    "Quantidade insuficiente em estoque."
            );
        }

        this.quantidadeAtual -= quantidade;
    }
}