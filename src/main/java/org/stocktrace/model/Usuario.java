package org.stocktrace.model;

public class Usuario {

    private String nome;
    private String login;
    private boolean ativo;

    public Usuario(String nome, String login) {
        this.nome = nome;
        this.login = login;
        this.ativo = true;
    }

    public String getNome() {return nome;}
    public String getLogin() {return login;}
    public boolean isAtivo() {return ativo;}


    //METODOS
    public void atualizarDados(String nome, String login) {
        this.nome = nome;
        this.login = login;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }
}