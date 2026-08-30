package org.example;
import java.time.LocalDate;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        // ⚠️ Código temporário de teste manual da Fase 1 (Java + POO).
        // Serve só pra validar Produto, ProdutoAlimenticio, ProdutoEletronico
        // e EstoqueInsuficienteException enquanto o projeto ainda está em memória.
        // Será substituído por testes de verdade (JUnit) e por um menu real mais pra frente.

        ProdutoAlimenticio pa = new ProdutoAlimenticio(1,10,"Biscoito de polvilho", LocalDate.of(2027,12,30));

        ProdutoEletronico pe = new ProdutoEletronico(2,1500.00,"Notebook",12);

        pa.adicionarEstoque(10);
        System.out.println(pa.getQuantidade());

        pe.adicionarEstoque(20);
        System.out.println(pe.getQuantidade());

       try{
           pe.retiraEstoque(30);
       } catch (EstoqueInsuficienteException e){
           System.out.println("ERRO: "+ e.getMessage());
       }

    }
}
