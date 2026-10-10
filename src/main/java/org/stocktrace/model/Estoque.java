package org.stocktrace.model;

import jakarta.persistence.*;
import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.exception.EstoqueInsuficienteException;

@Entity
@Table(name = "estoque")
public class Estoque {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "produto_id", nullable = false)
    private Produto produto;

    @ManyToOne(optional = false)
    @JoinColumn(name = "loja_id", nullable = false)
    private Loja loja;

    @Column(name = "quantidade_atual")
    private int quantidadeAtual;

    protected Estoque() {
    }

    // Estoque novo
    public Estoque(Produto produto, Loja loja) {
        this.produto = produto;
        this.loja = loja;
        this.quantidadeAtual = 0;
    }

    // Estoque que já veio do banco
    public Estoque(
            Long id,
            Produto produto,
            Loja loja,
            int quantidadeAtual
    ) {
        this.id = id;
        this.produto = produto;
        this.loja = loja;
        this.quantidadeAtual = quantidadeAtual;
    }

    public Long getId() {
        return id;
    }

    public Produto getProduto() {
        return produto;
    }

    public Loja getLoja() {
        return loja;
    }

    public int getQuantidadeAtual() {
        return quantidadeAtual;
    }

    public void adicionar(int quantidade) {

        if (quantidade <= 0) {
            throw new DadosInvalidosException(
                    "A quantidade adicionada deve ser maior que zero."
            );
        }

        this.quantidadeAtual += quantidade;
    }

    public void retirar(int quantidade) {

        if (quantidade <= 0) {
            throw new DadosInvalidosException(
                    "A quantidade retirada deve ser maior que zero."
            );
        }

        if (quantidade > this.quantidadeAtual) {
            throw new EstoqueInsuficienteException(
                    "Quantidade insuficiente em estoque."
            );
        }

        this.quantidadeAtual -= quantidade;
    }
}