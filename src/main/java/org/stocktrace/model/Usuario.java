package org.stocktrace.model;

public class Usuario {

    private Long id;
    private String nome;

    // Usuário novo
    public Usuario(String nome) {
        this.nome = nome;
    }

    // Usuário que já veio do banco
    public Usuario(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }
}