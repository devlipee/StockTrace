package org.stocktrace.model;

import java.math.BigDecimal;

public class Produto {

    private String codigo;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String unidadeMedida;
    private boolean ativo;
    private Categoria categoria;

    public Produto(String codigo, String nome, String descricao, BigDecimal preco, String unidadeMedida, Categoria categoria) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.unidadeMedida = unidadeMedida;
        this.categoria = categoria;
        this.ativo = true;
    }

    public String getCodigo() {return codigo;}
    public String getNome() {return nome;}
    public String getDescricao() {return descricao;}
    public BigDecimal getPreco() {return preco;}
    public String getUnidadeMedida() {return unidadeMedida;}
    public boolean isAtivo() {return ativo;}
    public Categoria getCategoria() {return categoria;}


    //METODOS
    public void atualizarDados(String nome, String descricao, BigDecimal preco, String unidadeMedida) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.unidadeMedida = unidadeMedida;
    }

    public void alterarCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }
}