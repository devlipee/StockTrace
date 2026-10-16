package org.stocktrace.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table (name = "loja")
public class Loja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    private String nome;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String complemento;
    private String cep;

    protected Loja() {
    }

    // Loja nova, ainda não cadastrada no banco.
    public Loja(
            Usuario usuario,
            String nome,
            String cidade,
            String bairro,
            String rua,
            String numero,
            String complemento,
            String cep
    ) {
        this.usuario = usuario;
        this.nome = nome;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.cep = cep;
    }



    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getCidade() {
        return cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public String getRua() {
        return rua;
    }

    public String getNumero() {
        return numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public String getCep() {
        return cep;
    }

    public Usuario getUsuario() {return usuario;}


    /*
     * Atualiza os dados da loja dentro do objeto Java.
     *
     * Depois disso, o LojaRepository pode salvar
     * esses novos valores no banco.
     */
    public void atualizarDados(
            String nome,
            String cidade,
            String bairro,
            String rua,
            String numero,
            String complemento,
            String cep
    ) {
        this.nome = nome;
        this.cidade = cidade;
        this.bairro = bairro;
        this.rua = rua;
        this.numero = numero;
        this.complemento = complemento;
        this.cep = cep;
    }


    public String getEnderecoCompleto() {
        return rua
                + ", "
                + numero
                + " - "
                + bairro
                + " - "
                + cidade;
    }
}