package org.example;
import org.example.exception.EstoqueInsuficienteException;
import org.example.model.Estoque;
import org.example.model.ProdutoAlimenticio;
import org.example.model.ProdutoEletronico;
import org.example.model.ItemEstoque;

import java.time.LocalDate;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

        //cria scaner e inicia estoque
        Scanner scanner = new Scanner(System.in);
        Estoque estoque = new Estoque();
        int opcao = -1;

        while (opcao != 0){
            System.out.println("1 - Cadastrar produto");
            System.out.println("2 - Listar produtos    ");
            System.out.println("3 - Adicionar estoque");
            System.out.println("4 - Retirar estoque");
            System.out.println("5 - Buscar produto por nome ");
            System.out.println("6 - Ver histórico de movimentações");
            System.out.println("0 - Sair");

            opcao = scanner.nextInt();

            switch (opcao){
                case 1:

            }

        }

    }
}