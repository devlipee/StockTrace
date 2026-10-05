package org.stocktrace.service;

import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.model.Movimentacao;
import org.stocktrace.repository.MovimentacaoRepository;

import java.util.List;
import java.io.IOException;
import java.sql.SQLException;

public class MovimentacaoService {

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

}
