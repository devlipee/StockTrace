package org.example.model;

public abstract class Produto {
    //atributos
    private int id;
    private String nome;
    private double preco;

    public Produto(int id, double preco, String nome) {
        this.id = id;
        this.preco = preco;
        this.nome = nome;
    }
    //getters
    public String getNome() {
        return nome;
    }

    public int getId() {
        return id;
    }

    public double getPreco() {
        return preco;
    }

    //setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "ID: "+ id +
                " | Nome: " + nome +
                " | Preço: R$ " + preco ;
    }
}