package org.stocktrace.service;

import org.stocktrace.exception.DadosInvalidosException;
import org.stocktrace.model.CategoriaProduto;
import org.stocktrace.model.Produto;
import org.stocktrace.repository.ProdutoRepository;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public class ProdutoService {

    private final ProdutoRepository produtoRepository = new ProdutoRepository();

    public Produto cadastrarProduto(
            String codigo,
            String nome,
            String descricao,
            BigDecimal preco,
            String unidadeMedida,
            CategoriaProduto categoria
    ) throws SQLException, IOException {

        //metodo que valida toda entrada de dados do produto
       validarDadosProduto(
               codigo,
               nome,
               descricao,
               preco,
               unidadeMedida,
               categoria);

        // Consulta se o código já está cadastrado.
        if (produtoRepository.existeProdutoPorCodigo(codigo)) {
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
        return  produtoRepository.cadastrarProduto(produto);
    }

    public List<Produto> listaProdutos() throws SQLException, IOException{
        return produtoRepository.listarProdutos();
    }

    public Produto buscarProdutoPorId(Long id) throws SQLException,IOException{

        if (id == null || id <= 0) {
            throw new DadosInvalidosException(
                    "O ID informado é inválido: " + id
            );
        }
        return produtoRepository.buscarProdutoPorId(id);
    }

    public Produto buscarProdutoPorCodigo(String codigo) throws SQLException, IOException{
        if (codigo == null || codigo.isBlank()){
            throw new DadosInvalidosException(
                    "O código do produto é obrigatório."
            );
        }
        return produtoRepository.buscarProdutoPorCodigo(codigo);
    }

    public Produto atualizarProduto(
            Long id,
            String nome,
            String descricao,
            BigDecimal preco,
            String unidadeMedida
    ) throws SQLException, IOException {

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
        produtoRepository.atualizarProduto(produto);

        return produto;
    }

    public Produto alterarCategoriaProduto(
            Long id,
            CategoriaProduto categoria
    ) throws SQLException, IOException{

        if (categoria == null){
            throw new DadosInvalidosException("A categoria do produto é obrigatória");
        }

        Produto produto = buscarProdutoPorId(id);
        produto.alterarCategoria(categoria);
        produtoRepository.atualizarProduto(produto);

        return produto;
    }

    public Produto ativarProduto(Long id) throws SQLException, IOException{
        Produto produto = buscarProdutoPorId(id);
        produto.ativar();
        produtoRepository.atualizarProduto(produto);
        return produto;

    }

    public Produto desativarProduto(Long id)throws SQLException, IOException{
        Produto produto = buscarProdutoPorId(id);
        produto.desativar();
        produtoRepository.atualizarProduto(produto);
        return produto;
    }

    public void  deletarProduto(Long id)throws SQLException, IOException{

        if (id == null || id <= 0){
            throw new DadosInvalidosException ("Id informado inválido, ID: "+ id);
        }
        produtoRepository.deletarProduto(id);
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
