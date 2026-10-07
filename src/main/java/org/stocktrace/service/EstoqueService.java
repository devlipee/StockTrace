package org.stocktrace.service;

import org.springframework.stereotype.Service;
import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Loja;
import org.stocktrace.model.Produto;
import org.stocktrace.repository.EstoqueRepository;


import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@Service
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoService produtoService;
    private final LojaService lojaService;

    public EstoqueService(
            EstoqueRepository estoqueRepository,
            ProdutoService produtoService,
            LojaService lojaService
    ) {
        this.estoqueRepository = estoqueRepository;
        this.produtoService = produtoService;
        this.lojaService = lojaService;
    }



  public List<Estoque> listarEstoques()throws SQLException, IOException{
    List<Estoque> estoques = estoqueRepository.listarEstoques();
      return estoques;
  }

  public Estoque buscarEstoquePorId(Long id)throws SQLException, IOException{
      if (id == null || id<=0){
          throw new DadosInvalidosException("O ID informado é inválido: " + id);
      }
     return estoqueRepository.buscarEstoquePorId(id);
  }

  public  Estoque buscarEstoquePorProdutoELoja(  Long produtoId,  Long lojaId) throws SQLException, IOException {

      produtoService.buscarProdutoPorId(produtoId);
      lojaService.buscarLojaPorId(lojaId);

      Estoque estoque = estoqueRepository.buscarEstoquePorProdutoELoja(produtoId,lojaId);

    return estoque;

  }

    public Estoque cadastrarEstoque(
            Long produtoId,
            Long lojaId
    ) throws SQLException, IOException {
      Produto produto = produtoService.buscarProdutoPorId(produtoId);
      Loja loja = lojaService.buscarLojaPorId(lojaId);

     Estoque estoqueExistente = estoqueRepository.buscarEstoquePorProdutoELoja(produtoId, lojaId);

     if(estoqueExistente != null){
         throw new DadosInvalidosException("Já existe estoque desse produto nessa loja");
     }

     Estoque estoque = new Estoque(produto, loja);
     Estoque estoqueSalvo = estoqueRepository.cadastrarEstoque(estoque);

        return estoqueSalvo;
    }







}
