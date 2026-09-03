package org.example;
import org.example.exception.EstoqueInsuficienteException;
import org.example.model.Estoque;
import org.example.model.ProdutoAlimenticio;
import org.example.model.ProdutoEletronico;
import org.example.model.ItemEstoque;

import java.time.LocalDate;


public class Main {
    public static void main(String[] args) {

        // ⚠️ Código temporário de teste manual da Fase 1 (Java + POO).
        // Serve só pra validar Produto, ProdutoAlimenticio, ProdutoEletronico,
        // Estoque e as exceptions enquanto o projeto ainda está em memória.
        // Será substituído por testes de verdade (JUnit) e por um menu real mais pra frente.

        ProdutoAlimenticio pa = new ProdutoAlimenticio(1,10,"Biscoito de polvilho", LocalDate.of(2027,12,30));
        ProdutoEletronico pe = new ProdutoEletronico(2,1500.00,"Notebook",12);

        Estoque estoque = new Estoque();
        estoque.cadastrarProduto(pa);
        estoque.cadastrarProduto(pe);

        estoque.adicionarEstoque(pa.getId(), 10);
        System.out.println(estoque.consultarQuantidade(pa.getId()));

        estoque.adicionarEstoque(pe.getId(), 20);
        System.out.println(estoque.consultarQuantidade(pe.getId()));

        try{
            estoque.retiraEstoque(pe.getId(), 30);
        } catch (EstoqueInsuficienteException e){
            System.out.println("ERRO: "+ e.getMessage());
        }
        System.out.println("--- Lista de produtos ---");
        estoque.listarProdutos();

        ItemEstoque encontrado = estoque.buscarItemPorNome("biscoito de polvilho");
        System.out.println(encontrado.getProduto() + " | Quantidade: " + encontrado.getQuantidade());
        System.out.println("--- Histórico de movimentações ---");
        estoque.retiraEstoque(1,1);
        estoque.listarMovimentacoes();


    }
}