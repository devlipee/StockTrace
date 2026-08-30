package org.example.model;

import java.time.LocalDate;

public class ProdutoAlimenticio extends Produto {

    private LocalDate dataValidade;

    public ProdutoAlimenticio(int id, double preco, String nome, LocalDate dataValidade) {
        super(id, preco, nome);
        this.dataValidade = dataValidade;
    }

    public LocalDate getDataValidade() {
        return dataValidade;
    }

    @Override
    public String  toString() {
        return super.toString() +
                " | Data de validade: " + dataValidade;
    }
}
