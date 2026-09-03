package org.example.model;


import org.example.exception.ProdutoNaoEncontradoException;
import java.time.LocalDate;

import java.util.ArrayList;

public class Estoque {

    private ArrayList<ItemEstoque> itens;
    private  ArrayList<Movimentacao> historico;
    private int proximoId;

    public Estoque() {
        this.itens = new ArrayList<>();
        this.historico = new ArrayList<>();
        this.proximoId = 1;
    }

    public void cadastrarProdutoAlimenticio(String nome, double preco, LocalDate dataValidade){
        ProdutoAlimenticio produto = new ProdutoAlimenticio(proximoId, preco, nome, dataValidade);
        itens.add(new ItemEstoque(produto));
        proximoId++;
    }

    public void cadastrarProdutoEletronico(String nome, double preco, int mesesGarantia){
        ProdutoEletronico produto = new ProdutoEletronico(proximoId, preco, nome, mesesGarantia);
        itens.add(new ItemEstoque(produto));
        proximoId++;
    }


    public ItemEstoque buscarItemPorId(int id){
        for (ItemEstoque item : itens){
            if (item.getProduto().getId() == id){
                return item;
            }
        }
        throw new ProdutoNaoEncontradoException("ERRO: Nenhum produto encontrado com esse ID!");
    }

    public void adicionarEstoque(int idProduto, int quantidade){
        ItemEstoque item = buscarItemPorId(idProduto);
        item.adicionarEstoque(quantidade);
        Movimentacao mov = new Movimentacao(item.getProduto(), quantidade, TipoMovimentacao.ENTRADA);
        historico.add(mov);

    }

    public void retiraEstoque(int idProduto, int quantidade){
        ItemEstoque item = buscarItemPorId(idProduto);
        item.retiraEstoque(quantidade);
        Movimentacao mov = new Movimentacao(item.getProduto(), quantidade, TipoMovimentacao.SAIDA);
        historico.add(mov);

    }

    public int consultarQuantidade(int idProduto){
        return buscarItemPorId(idProduto).getQuantidade();
    }

    public void listarProdutos(){
        for (ItemEstoque item : itens){
            System.out.println(item.getProduto()+ " | Quantidade: "+ item.getQuantidade());;
        }
    }
    public void listarMovimentacoes(){
        for (Movimentacao mov : historico){
            System.out.println(mov);
        }

    }

    public ItemEstoque buscarItemPorNome(String nome) {

        for (ItemEstoque item : itens) {

            if (item.getProduto().getNome().equalsIgnoreCase(nome)) {
                return item;
            }
        }

        throw new ProdutoNaoEncontradoException(
                "ERRO: Produto " + nome + " não encontrado!"
        );
    }



}