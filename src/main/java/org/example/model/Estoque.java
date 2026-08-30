package org.example.model;

import org.example.exception.ProdutoDuplicadoException;
import org.example.exception.ProdutoNaoEncontradoException;

import java.util.ArrayList;

public class Estoque {

    private ArrayList<ItemEstoque> itens;

    public Estoque() {
        this.itens = new ArrayList<>();
    }

    public void cadastrarProduto(Produto produto){
        for (ItemEstoque item : itens){
            if (item.getProduto().getId() == produto.getId()){
                throw new ProdutoDuplicadoException("ERRO: Produto cadastrado com ID igual há outro produto existente!");
            }
        }
        itens.add(new ItemEstoque(produto));
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
    }

    public void retiraEstoque(int idProduto, int quantidade){
        ItemEstoque item = buscarItemPorId(idProduto);
        item.retiraEstoque(quantidade);
    }

    public int consultarQuantidade(int idProduto){
        return buscarItemPorId(idProduto).getQuantidade();
    }

    public void listarProdutos(){
        for (ItemEstoque item : itens){
            System.out.println(item.getProduto()+ " | Quantidade: "+ item.getQuantidade());;
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