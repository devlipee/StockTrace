package org.stocktrace.model;

public class Loja {

    private Long id;
    private String nome;
    private String cidade;
    private String bairro;
    private String rua;
    private String numero;
    private String complemento;
    private String cep;


    // Loja nova, ainda não cadastrada no banco.
    public Loja(
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


    // Loja que já veio do banco.
    public Loja(
            Long id,
            String nome,
            String cidade,
            String bairro,
            String rua,
            String numero,
            String complemento,
            String cep
    ) {
        this.id = id;
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