package org.stocktrace;

import org.stocktrace.exception.StockTraceException;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Movimentacao;
import org.stocktrace.model.TipoMovimentacao;
import org.stocktrace.service.EstoqueService;
import org.stocktrace.service.MovimentacaoService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        EstoqueService estoqueService = new EstoqueService();
        MovimentacaoService movimentacaoService = new MovimentacaoService();

        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("TESTE DE ENTRADA DE ESTOQUE");
            System.out.println("Este teste grava uma entrada real de 5 unidades no banco.");
            System.out.print("Informe o ID de um estoque existente: ");
            Long estoqueId = Long.parseLong(scanner.nextLine().trim());

            System.out.print("Informe o nome do responsável: ");
            String responsavel = scanner.nextLine();

            Estoque estoqueAntes = estoqueService.buscarEstoquePorId(estoqueId);
            int saldoAntes = estoqueAntes.getQuantidadeAtual();
            int historicoAntes = movimentacaoService
                    .listarMovimentacoesPorEstoque(estoqueId).size();

            System.out.println("Produto: " + estoqueAntes.getProduto().getNome());
            System.out.println("Loja: " + estoqueAntes.getLoja().getNome());
            System.out.println("Saldo antes: " + saldoAntes);

            Movimentacao entrada = movimentacaoService.registrarEntrada(
                    estoqueId,
                    5,
                    "Reposição",
                    null,
                    responsavel
            );

            // A entrada já foi confirmada. As consultas abaixo conferem o banco.
            System.out.println("Entrada gravada. ID: " + entrada.getId());

            Estoque estoqueDepois = estoqueService.buscarEstoquePorId(estoqueId);
            Movimentacao entradaNoBanco = movimentacaoService
                    .buscarMovimentacaoPorId(entrada.getId());
            int historicoDepois = movimentacaoService
                    .listarMovimentacoesPorEstoque(estoqueId).size();

            boolean saldoCorreto = estoqueDepois.getQuantidadeAtual() == (long) saldoAntes + 5;
            boolean historicoCorreto = historicoDepois == historicoAntes + 1
                    && entradaNoBanco.getTipo() == TipoMovimentacao.ENTRADA
                    && entradaNoBanco.getQuantidade() == 5
                    && entradaNoBanco.getEstoque().getId().equals(estoqueId);

            System.out.println("Saldo depois: " + estoqueDepois.getQuantidadeAtual());
            System.out.println("Movimentações antes: " + historicoAntes);
            System.out.println("Movimentações depois: " + historicoDepois);
            System.out.println("Saldo: " + (saldoCorreto ? "OK" : "DIVERGENTE"));
            System.out.println("Histórico: " + (historicoCorreto ? "OK" : "DIVERGENTE"));

            if (saldoCorreto && historicoCorreto) {
                System.out.println("TESTE CONCLUÍDO COM SUCESSO.");
            } else {
                System.out.println("Confira os dados. Execute o teste sem outras movimentações simultâneas.");
            }

        } catch (NumberFormatException erro) {
            System.out.println("Informe um ID numérico válido.");
        } catch (StockTraceException erro) {
            System.out.println("Regra de negócio: " + erro.getMessage());
        } catch (SQLException | IOException erro) {
            System.out.println("Não foi possível concluir o teste por um erro de banco ou configuração.");
            System.out.println("Tipo do erro: " + erro.getClass().getSimpleName());
            System.out.println("Se a entrada já foi gravada, uma falha na conferência não a desfaz.");
            System.out.println("Confira o banco antes de executar novamente.");
        }
    }
}