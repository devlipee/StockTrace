package org.stocktrace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimentacao")
public class Movimentacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "estoque_id", nullable = false)
    private Estoque estoque;

    @Enumerated(EnumType.STRING)
    private TipoMovimentacao tipo;

    private int quantidade;

    @Column(name = "data_hora")
    private LocalDateTime dataHora;

    private String motivo;
    private String observacao;
    private String responsavel;

    protected Movimentacao() {
    }


    // Movimentação nova
    public Movimentacao(
            Estoque estoque,
            TipoMovimentacao tipo,
            int quantidade,
            String motivo,
            String observacao,
            String responsavel
    ) {
        this.estoque = estoque;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.motivo = motivo;
        this.observacao = observacao;
        this.responsavel = responsavel;
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
            String responsavel
    ) {
        this.id = id;
        this.estoque = estoque;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.dataHora = dataHora;
        this.motivo = motivo;
        this.observacao = observacao;
        this.responsavel = responsavel;
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

    public String getResponsavel() {
        return responsavel;
    }

    public boolean ehEntrada() {
        return this.tipo == TipoMovimentacao.ENTRADA;
    }

    public boolean ehSaida() {
        return this.tipo == TipoMovimentacao.SAIDA;
    }

    @JsonIgnore
    public String getDescricao() {
        return this.tipo
                + " de "
                + this.quantidade
                + " unidade(s) do produto "
                + this.estoque.getProduto().getNome()
                + " na loja "
                + this.estoque.getLoja().getNome()
                + " - Responsável: "
                + this.responsavel;
    }
}