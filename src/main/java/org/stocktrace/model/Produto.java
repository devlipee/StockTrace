package org.stocktrace.model;

import java.math.BigDecimal;

public class Produto {

    private Long id;
    private String codigo;
    private String nome;
    private String descricao;
    private BigDecimal preco;
    private String unidadeMedida;
    private boolean ativo;
    private CategoriaProduto categoria;

    // Produto novo
    public Produto(
            String codigo,
            String nome,
            String descricao,
            BigDecimal preco,
            String unidadeMedida,
            CategoriaProduto categoria
    ) {
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.unidadeMedida = unidadeMedida;
        this.categoria = categoria;
        this.ativo = true;
    }

    // Produto que já veio do banco
    public Produto(
            Long id,
            String codigo,
            String nome,
            String descricao,
            BigDecimal preco,
            String unidadeMedida,
            boolean ativo,
            CategoriaProduto categoria

    ) {
        this.id = id;
        this.codigo = codigo;
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.unidadeMedida = unidadeMedida;
        this.ativo = ativo;
        this.categoria = categoria;
    }

    public Long getId() {
        return id;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public BigDecimal getPreco() {
        return preco;
    }

    public String getUnidadeMedida() {
        return unidadeMedida;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public CategoriaProduto getCategoria() {
        return categoria;
    }

    public void atualizarDados(
            String nome,
            String descricao,
            BigDecimal preco,
            String unidadeMedida
    ) {
        this.nome = nome;
        this.descricao = descricao;
        this.preco = preco;
        this.unidadeMedida = unidadeMedida;
    }

    public void alterarCategoria( CategoriaProduto categoria) {
        this.categoria = categoria;
    }

    public void ativar() {
        this.ativo = true;
    }

    public void desativar() {
        this.ativo = false;
    }
}