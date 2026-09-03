package org.example.model;

import java.time.LocalDateTime;

public class Movimentacao {

    private Produto produto;
    private int quantidade;
    private TipoMovimentacao tipo;
    private LocalDateTime dataHora;

    public Movimentacao(Produto produto, int quantidade, TipoMovimentacao tipo) {
        this.produto = produto;
        this.quantidade = quantidade;
        this.tipo = tipo;
        this.dataHora = LocalDateTime.now();
    }

    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    @Override
    public String toString() {
        return "[" + dataHora + "] " + tipo + " | " + produto.getNome() + " | Quantidade: " + quantidade;
    }
}