package org.stocktrace.model;

import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table (name = "produto")
public class Produto {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String codigo;

    @Column(length = 100)
    private String nome;

    @Column(length = 255)
    private String descricao;

    @Column(precision = 10, scale = 2)
    private BigDecimal preco;

    @Column(name = "unidade_medida", length = 20)
    private String unidadeMedida;

    private boolean ativo;

    @Enumerated(EnumType.STRING)
    @Column(length = 30)
    private CategoriaProduto categoria;

    protected Produto() {
    }


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