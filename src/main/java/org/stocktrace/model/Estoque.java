package org.stocktrace.model;

import org.stocktrace.exception.EstoqueInsuficienteException;
import org.stocktrace.exception.QuantidadeInvalidaException;

public class Estoque {

    private final Produto produto;
    private final Localizacao localizacao;
    private int quantidadeAtual;

    public Estoque(Produto produto, Localizacao localizacao) {
        this.produto = produto;
        this.localizacao = localizacao;
        this.quantidadeAtual = 0;
    }

    public Produto getProduto() {return produto;}
    public Localizacao getLocalizacao() {return localizacao;}
    public int getQuantidadeAtual() {return quantidadeAtual;}

    //METODOS

    //Adiciona produto no estoque se a quantidade inserida for maior q zero
    public void adicionar(int quantidade) {
        if (quantidade <= 0) {
            throw new QuantidadeInvalidaException(
                    "A quantidade adicionada deve ser maior que zero."
            );
        }
        this.quantidadeAtual += quantidade;
    }

    //Retira produto do estoque se a retirada for maior q zero e verifica se tem estoque suficiente para retirar
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