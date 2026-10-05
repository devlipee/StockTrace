package org.stocktrace.service;

import org.stocktrace.config.ConexaoBanco;
import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Movimentacao;
import org.stocktrace.model.TipoMovimentacao;
import org.stocktrace.repository.EstoqueRepository;
import org.stocktrace.repository.MovimentacaoRepository;

import java.sql.Connection;
import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

public class MovimentacaoService {
    private final EstoqueRepository estoqueRepository = new EstoqueRepository();
    private final EstoqueService estoqueService = new EstoqueService();
    private final MovimentacaoRepository movimentacaoRepository = new MovimentacaoRepository();

    public List<Movimentacao> listarMovimentacoes()throws SQLException, IOException {
        return movimentacaoRepository.listarMovimentacoes();
    }

    public Movimentacao buscarMovimentacaoPorId(Long id) throws SQLException, IOException{
        if (id == null || id<= 0){
            throw new DadosInvalidosException("Id informado inválido, ID: " + id);
        }

        return movimentacaoRepository.buscarMovimentacaoPorId(id);
    }

    public List<Movimentacao> listarMovimentacoesPorEstoque(Long estoqueId)
            throws SQLException, IOException {

        estoqueService.buscarEstoquePorId(estoqueId);

        return movimentacaoRepository.listarMovimentacoesPorEstoque(estoqueId);
    }

    public Movimentacao registrarEntrada(
            Long estoqueId,
            int quantidade,
            String motivo,
            String observacao,
            String responsavel
    ) throws SQLException, IOException {

        validarDadosMovimentacao(quantidade, motivo, responsavel);

        if (estoqueId == null || estoqueId <= 0) {
            throw new DadosInvalidosException(
                    "O ID do estoque é inválido: " + estoqueId
            );
        }

        try (Connection conexao = ConexaoBanco.conectar()) {
            conexao.setAutoCommit(false);

            try {
                // Busca o estoque e mantém a linha bloqueada na transação.
                Estoque estoque = estoqueRepository.buscarEstoquePorIdParaAtualizacao(
                                conexao,
                                estoqueId
                        );

                // Evita ultrapassar o limite de quantidade do tipo int.
                if ((long) estoque.getQuantidadeAtual() + quantidade
                        > Integer.MAX_VALUE) {
                    throw new DadosInvalidosException(
                            "A entrada ultrapassa a quantidade máxima suportada."
                    );
                }

                // Atualiza a quantidade apenas no objeto Java.
                estoque.adicionar(quantidade);

                // Cria o registro que explica a alteração do estoque.
                Movimentacao movimentacao = new Movimentacao(
                        estoque,
                        TipoMovimentacao.ENTRADA,
                        quantidade,
                        motivo,
                        observacao,
                        responsavel
                );

                // Salva o saldo usando a conexão da transação.
                estoqueRepository.atualizarQuantidade(conexao, estoque);

                // Salva o histórico usando a mesma conexão.
                Movimentacao movimentacaoSalva =
                        movimentacaoRepository.cadastrarMovimentacao(
                                conexao,
                                movimentacao
                        );

                // Confirma as duas gravações juntas.
                conexao.commit();

                return movimentacaoSalva;

            } catch (SQLException | IOException | RuntimeException erro) {
                try {
                    // Desfaz as alterações ainda não confirmadas no banco.
                    conexao.rollback();
                } catch (SQLException erroRollback) {
                    erro.addSuppressed(erroRollback);
                }

                throw erro;
            }
        }
    }






    private void validarDadosMovimentacao(int quantidade, String motivo, String responsavel){

        if(quantidade <= 0){
            throw new DadosInvalidosException("A quantidade deve ser maior que zero.");
        }

        if (motivo == null || motivo.isBlank()){
            throw new DadosInvalidosException("O motivo é obrigatório.");
        }

        if (responsavel == null || responsavel.isBlank()){
            throw new DadosInvalidosException("O responsável é obrigatório.");
        }

    }
}
