package org.stocktrace.service;

import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.model.Estoque;
import org.stocktrace.model.Loja;
import org.stocktrace.model.Produto;
import org.stocktrace.repository.EstoqueRepository;
import org.stocktrace.repository.ProdutoRepository;
import org.stocktrace.service.ProdutoService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class EstoqueService {

  private final EstoqueRepository estoqueRepository = new EstoqueRepository();
  private final ProdutoService produtoService = new ProdutoService();
  private final LojaService lojaService = new LojaService();


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
