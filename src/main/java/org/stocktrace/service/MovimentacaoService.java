package org.stocktrace.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.exception.EntidadeNaoEncontradaException;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Movimentacao;
import org.stocktrace.model.TipoMovimentacao;
import org.stocktrace.repository.EstoqueJpaRepository;
import org.stocktrace.repository.MovimentacaoJpaRepository;
import java.util.List;


@Service
public class MovimentacaoService {

    private final EstoqueJpaRepository estoqueJpaRepository;
    private final EstoqueService estoqueService;
    private final MovimentacaoJpaRepository movimentacaoJpaRepository;

    public MovimentacaoService(
            EstoqueJpaRepository estoqueJpaRepository,
            EstoqueService estoqueService,
            MovimentacaoJpaRepository movimentacaoJpaRepository
    ){
        this.estoqueJpaRepository = estoqueJpaRepository;
        this.estoqueService = estoqueService;
        this.movimentacaoJpaRepository = movimentacaoJpaRepository;
    }

    public List<Movimentacao> listarMovimentacoes() {
        return movimentacaoJpaRepository.findAll();
    }

    public Movimentacao buscarMovimentacaoPorId(Long id){
        if (id == null || id<= 0){
            throw new DadosInvalidosException("Id informado inválido, ID: " + id);
        }

        return movimentacaoJpaRepository.findById(id).orElseThrow(()-> new EntidadeNaoEncontradaException(
                "Movimentação não encontrada. ID: "+id
        ));
    }

    public List<Movimentacao> listarMovimentacoesPorEstoque(Long estoqueId) {

        estoqueService.buscarEstoquePorId(estoqueId);

        return movimentacaoJpaRepository.findByEstoque_Id(estoqueId)  ;
    }

    @Transactional
    public Movimentacao registrarEntrada(
            Long estoqueId,
            int quantidade,
            String motivo,
            String observacao,
            String responsavel
    ) {
        validarDadosMovimentacao(quantidade, motivo, responsavel);

        if (estoqueId == null || estoqueId <= 0) {
            throw new DadosInvalidosException(
                    "O ID do estoque é inválido: " + estoqueId
            );
        }
        // Carrega o estoque com lock pessimista (SELECT ... FOR UPDATE).
        Estoque estoque = estoqueJpaRepository.buscarPorIdParaAtualizacao(estoqueId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Estoque não encontrado. ID: " + estoqueId
                ));

        // Evita ultrapassar o limite de quantidade do tipo int.
        if ((long) estoque.getQuantidadeAtual() + quantidade > Integer.MAX_VALUE) {
            throw new DadosInvalidosException(
                    "A entrada ultrapassa a quantidade máxima suportada."
            );
        }

        // Entidade gerenciada: o Hibernate grava o novo saldo no commit.
        estoque.adicionar(quantidade);

        Movimentacao movimentacao = new Movimentacao(
                estoque,
                TipoMovimentacao.ENTRADA,
                quantidade,
                motivo,
                observacao,
                responsavel
        );

        return movimentacaoJpaRepository.save(movimentacao);
    }


    @Transactional
    public Movimentacao registrarSaida(
            Long estoqueId,
            int quantidade,
            String motivo,
            String observacao,
            String responsavel
    ) {
        validarDadosMovimentacao(quantidade, motivo, responsavel);

        if (estoqueId == null || estoqueId <= 0) {
            throw new DadosInvalidosException(
                    "O ID do estoque é inválido: " + estoqueId
            );
        }

        // Carrega o estoque com lock pessimista (SELECT ... FOR UPDATE).
        Estoque estoque = estoqueJpaRepository.buscarPorIdParaAtualizacao(estoqueId)
                .orElseThrow(() -> new EntidadeNaoEncontradaException(
                        "Estoque não encontrado. ID: " + estoqueId
                ));

        // O model verifica se há saldo suficiente antes de retirar.
        estoque.retirar(quantidade);

        Movimentacao movimentacao = new Movimentacao(
                estoque,
                TipoMovimentacao.SAIDA,
                quantidade,
                motivo,
                observacao,
                responsavel
        );

        // Entidade gerenciada: o Hibernate grava o novo saldo no commit.
        return movimentacaoJpaRepository.save(movimentacao);
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
