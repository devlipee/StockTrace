package org.example;

public abstract class Produto {
    //atributos
    private int id;
    private String nome;
    private double preco;
    private int quantidadeEstoque;

    public Produto(int id, double preco, String nome) {
        this.id = id;
        this.quantidadeEstoque = 0;
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

    public int getQuantidade() {
        return quantidadeEstoque;
    }

    //setters
    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }
    //metodo que adiciona estoque
    public void adicionarEstoque(int quantidade){
        if (quantidade>0){
            this.quantidadeEstoque += quantidade;
            System.out.println(quantidade + " unidades adicionadas. Total atual: "+ this.quantidadeEstoque);
        }
        else {
            System.out.println("Quantidade adicionada deve ser maior que zero!");
        }
    }
    //metodo que retira estoque
    public void retiraEstoque(int quantidade){
        if (quantidade > 0 && quantidadeEstoque >= quantidade){
            this.quantidadeEstoque -= quantidade;
            System.out.println(quantidade + " unidades retiradas. Total atual: "+ this.quantidadeEstoque);
        }
        else {
            throw new EstoqueInsuficienteException("Quantidade adiconada deve ser menor ou igual ao ESTOQUE TOTAL!");

        }
    }
}
