package org.stocktrace.model;

public class Localizacao {

    private String nome;
    private String descricao;
    private boolean ativo;

    public Localizacao(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
        this.ativo = true;
    }

    public String getNome() {return nome;}
    public String getDescricao() {return descricao;}
    public boolean isAtivo() {return ativo;}


    //METODOS
    public void atualizarDados(String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }
}