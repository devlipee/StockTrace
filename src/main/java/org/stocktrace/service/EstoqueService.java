package org.stocktrace.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.exception.EntidadeNaoEncontradaException;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Loja;
import org.stocktrace.model.Produto;
import org.stocktrace.repository.EstoqueJpaRepository;

import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueJpaRepository estoqueJpaRepository;
    private final ProdutoService produtoService;
    private final LojaService lojaService;

    public EstoqueService(
            EstoqueJpaRepository estoqueJpaRepository,
            ProdutoService produtoService,
            LojaService lojaService
    ) {
        this.estoqueJpaRepository = estoqueJpaRepository;
        this.produtoService = produtoService;
        this.lojaService = lojaService;
    }



  public List<Estoque> listarEstoques(){
     return estoqueJpaRepository.findAll();
  }

  public Estoque buscarEstoquePorId(Long id){
      if (id == null || id<=0){
          throw new DadosInvalidosException("O ID informado é inválido: " + id);
      }
     return estoqueJpaRepository.findById(id).orElseThrow(()-> new EntidadeNaoEncontradaException(
             "Estoque não encontrado. ID: "+id
     ));
  }

  public  Estoque buscarEstoquePorProdutoELoja(  Long produtoId,  Long lojaId) {

      produtoService.buscarProdutoPorId(produtoId);
      lojaService.buscarLojaPorId(lojaId);

      return estoqueJpaRepository.findByProduto_IdAndLoja_Id(produtoId,lojaId).orElse(null) ;
  }

    @Transactional
    public Estoque cadastrarEstoque(
            Long produtoId,
            Long lojaId
    ) {
      Produto produto = produtoService.buscarProdutoPorId(produtoId);
      Loja loja = lojaService.buscarLojaPorId(lojaId);

     Estoque estoqueExistente = estoqueJpaRepository.findByProduto_IdAndLoja_Id(produtoId, lojaId).orElse(null);

     if(estoqueExistente != null){
         throw new DadosInvalidosException("Já existe estoque desse produto nessa loja");
     }

     Estoque estoque = new Estoque(produto, loja);
     Estoque estoqueSalvo = estoqueJpaRepository.save(estoque);

        return estoqueSalvo;
    }

}
