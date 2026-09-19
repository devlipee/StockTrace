package org.stocktrace;

import org.stocktrace.model.*;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {

        Categoria categoria = new Categoria(
                "Alimentos",
                "Produtos alimentícios"
        );

        Produto arroz = new Produto(
                "ARR001",
                "Arroz 5kg",
                "Arroz branco tipo 1",
                new BigDecimal("29.90"),
                "UN",
                categoria
        );

        Localizacao deposito = new Localizacao(
                "Depósito",
                "Área principal"
        );

        Usuario usuario = new Usuario(
                "Samuel",
                "luiz"
        );

        Estoque estoque = new Estoque(
                arroz,
                deposito
        );

        estoque.adicionar(50);

        Movimentacao entrada = new Movimentacao(
                estoque,
                TipoMovimentacao.ENTRADA,
                50,
                "Compra de fornecedor",
                "Estoque inicial",
                usuario
        );

        System.out.println("Produto: " + arroz.getNome());
        System.out.println("Localização: " + deposito.getNome());
        System.out.println("Quantidade: " + estoque.getQuantidadeAtual());
        System.out.println(entrada.getDescricao());
    }
}