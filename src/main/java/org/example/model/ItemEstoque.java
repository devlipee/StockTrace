package org.example.model;

import org.example.exception.EstoqueInsuficienteException;

public class ItemEstoque {

    private Produto produto;
    private int quantidade;

    public ItemEstoque(Produto produto) {
        this.produto = produto;
        this.quantidade = 0;
    }

    //getters
    public Produto getProduto() {
        return produto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    //metodo que adiciona estoque
    public void adicionarEstoque(int quantidade) {
        if (quantidade > 0) {
            this.quantidade += quantidade;
            System.out.println(quantidade + " unidades adicionadas. Total atual: " + this.quantidade);
        } else {
            System.out.println("Quantidade adicionada deve ser maior que zero!");
        }
    }

    //metodo que retira estoque
    public void retiraEstoque(int quantidade) {
        if (quantidade > 0 && this.quantidade >= quantidade) {
            this.quantidade -= quantidade;
            System.out.println(quantidade + " unidades retiradas. Total atual: " + this.quantidade);
        } else {
            throw new EstoqueInsuficienteException("Quantidade retirada deve ser menor ou igual ao ESTOQUE TOTAL!");
        }
    }
}