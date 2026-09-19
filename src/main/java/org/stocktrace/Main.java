package org.stocktrace;

import org.stocktrace.model.CategoriaProduto;
import org.stocktrace.model.Produto;
import org.stocktrace.repository.ProdutoRepository;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        ProdutoRepository produtoRepository = new ProdutoRepository();

        try {

            produtoRepository.deletarProduto(2L);

            System.out.println("Produto deletado com sucesso!");

        } catch (Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }


    }
}