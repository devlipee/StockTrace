package org.stocktrace.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.exception.EntidadeNaoEncontradaException;
import org.stocktrace.model.CategoriaProduto;
import org.stocktrace.model.Produto;
import org.stocktrace.repository.ProdutoJpaRepository;


import java.math.BigDecimal;
import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoJpaRepository produtoJpaRepository;


    public ProdutoService(ProdutoJpaRepository produtoJpaRepository) {
        this.produtoJpaRepository = produtoJpaRepository;

    }

    public Produto cadastrarProduto(
            String codigo,
            String nome,
            String descricao,
            BigDecimal preco,
            String unidadeMedida,
            CategoriaProduto categoria
    ) {
        //metodo que valida toda entrada de dados do produto
       validarDadosProduto(
               codigo,
               nome,
               descricao,
               preco,
               unidadeMedida,
               categoria);

        // Consulta se o código já está cadastrado.
        if (produtoJpaRepository.existsByCodigo(codigo)) {
            throw new DadosInvalidosException(
                    "Já existe um produto cadastrado com esse código."
            );
        }
        Produto produto = new Produto(
                codigo,
                nome,
                descricao,
                preco,
                unidadeMedida,
                categoria
        );
        return  produtoJpaRepository.save(produto);
    }

    public List<Produto> listaProdutos(){
        return produtoJpaRepository.findAll();
    }

    public Produto buscarProdutoPorId(Long id){

        if (id == null || id <= 0) {
            throw new DadosInvalidosException(
                    "O ID informado é inválido: " + id
            );
        }
        return produtoJpaRepository.findById(id).orElseThrow(()-> new EntidadeNaoEncontradaException(
                "Produto não encontrado. ID: "+ id
        ));
    }

    public Produto buscarProdutoPorCodigo(String codigo){
        if (codigo == null || codigo.isBlank()){
            throw new DadosInvalidosException(
                    "O código do produto é obrigatório."
            );
        }
        return produtoJpaRepository.findByCodigo(codigo).orElseThrow(()-> new EntidadeNaoEncontradaException(
                "Produto não encontrado. Código: " + codigo
        ));
    }

    @Transactional
    public Produto atualizarProduto(
            Long id,
            String nome,
            String descricao,
            BigDecimal preco,
            String unidadeMedida
    ) {
        // Busca o produto e reaproveita a validação do ID.
        Produto produto = buscarProdutoPorId(id);

        // Valida os novos dados junto ao código e à categoria atuais.
        validarDadosProduto(
                produto.getCodigo(),
                nome,
                descricao,
                preco,
                unidadeMedida,
                produto.getCategoria()
        );

        // Altera os dados do objeto em memória.
        produto.atualizarDados(
                nome,
                descricao,
                preco,
                unidadeMedida
        );

        // Persiste as alterações no banco.
       return produtoJpaRepository.save(produto);
    }

    @Transactional
    public Produto alterarCategoriaProduto(
            Long id,
            CategoriaProduto categoria
    ) {

        if (categoria == null){
            throw new DadosInvalidosException("A categoria do produto é obrigatória");
        }

        Produto produto = buscarProdutoPorId(id);
        produto.alterarCategoria(categoria);
        return produtoJpaRepository.save(produto);

    }

    @Transactional
    public Produto ativarProduto(Long id){
        Produto produto = buscarProdutoPorId(id);
        produto.ativar();
        return produtoJpaRepository.save(produto);

    }

    @Transactional
    public Produto desativarProduto(Long id){
        Produto produto = buscarProdutoPorId(id);
        produto.desativar();
         return produtoJpaRepository.save(produto);
    }

    @Transactional
    public void  deletarProduto(Long id){
        Produto produto = buscarProdutoPorId(id);
        produtoJpaRepository.delete(produto);
    }

    // Validação completa dos dados do produto:

    private void validarDadosProduto(
            String codigo,
            String nome,
            String descricao,
            BigDecimal preco,
            String unidadeMedida,
            CategoriaProduto categoria
    ) {

        if (codigo == null || codigo.isBlank()) {
            throw new DadosInvalidosException(
                    "O código do produto é obrigatório."
            );
        }

        if (nome == null || nome.isBlank()) {
            throw new DadosInvalidosException(
                    "O nome do produto é obrigatório."
            );
        }

        if (descricao == null || descricao.isBlank()) {
            throw new DadosInvalidosException(
                    "A descrição do produto é obrigatória."
            );
        }

        //
        if (descricao.length() > 255) {
            throw new DadosInvalidosException(
                    "A descrição do produto deve ter no máximo 255 caracteres."
            );
        }

        if (preco == null) {
            throw new DadosInvalidosException(
                    "O preço do produto é obrigatório."
            );
        }

        if (preco.compareTo(BigDecimal.ZERO) <= 0) {
            throw new DadosInvalidosException(
                    "O preço do produto deve ser maior que zero."
            );
        }

        if (unidadeMedida == null || unidadeMedida.isBlank()) {
            throw new DadosInvalidosException(
                    "A unidade de medida do produto é obrigatória."
            );
        }

        if (categoria == null) {
            throw new DadosInvalidosException(
                    "A categoria do produto é obrigatória."
            );
        }
    }




}
