package org.stocktrace.model;

import java.time.LocalDateTime;

public class Movimentacao {

    private Long id;
    private Estoque estoque;
    private TipoMovimentacao tipo;
    private int quantidade;
    private LocalDateTime dataHora;
    private String motivo;
    private String observacao;
    private Usuario usuario;

    // Movimentação nova
    public Movimentacao(
            Estoque estoque,
            TipoMovimentacao tipo,
            int quantidade,
            String motivo,
            String observacao,
            Usuario usuario
    ) {
        this.estoque = estoque;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.motivo = motivo;
        this.observacao = observacao;
        this.usuario = usuario;
        this.dataHora = LocalDateTime.now();
    }

    // Movimentação que já veio do banco
    public Movimentacao(
            Long id,
            Estoque estoque,
            TipoMovimentacao tipo,
            int quantidade,
            LocalDateTime dataHora,
            String motivo,
            String observacao,
            Usuario usuario
    ) {
        this.id = id;
        this.estoque = estoque;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataHora = dataHora;
        this.motivo = motivo;
        this.observacao = observacao;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public Estoque getEstoque() {
        return estoque;
    }

    public TipoMovimentacao getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public String getMotivo() {
        return motivo;
    }

    public String getObservacao() {
        return observacao;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public boolean ehEntrada() {
        return this.tipo == TipoMovimentacao.ENTRADA;
    }

    public boolean ehSaida() {
        return this.tipo == TipoMovimentacao.SAIDA;
    }

    public String getDescricao() {
        return this.tipo
                + " de "
                + this.quantidade
                + " unidade(s) do produto "
                + this.estoque.getProduto().getNome()
                + " na loja "
                + this.estoque.getLoja().getNome();
    }
}