package org.stocktrace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stocktrace.dto.ProdutoAtualizadoDTO;
import org.stocktrace.dto.ProdutoCategoriaDTO;
import org.stocktrace.dto.ProdutoDTO;
import org.stocktrace.model.Produto;
import org.stocktrace.service.ProdutoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public List<Produto> listarProdutos(){
        return produtoService.listaProdutos();
    }

    @GetMapping("/{id}")
    public Produto buscarProdutoPorId(@PathVariable("id") Long id){
        return produtoService.buscarProdutoPorId(id);
    }

    @GetMapping("/codigo/{codigo}")
    public Produto buscarProdutoPorCodigo(@PathVariable("codigo") String codigo){
        return produtoService.buscarProdutoPorCodigo(codigo);
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody ProdutoDTO dados){

        Produto produto = produtoService.cadastrarProduto(
                dados.codigo(),
                dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.unidadeMedida(),
                dados.categoria()
        );

        URI endereco = URI.create(("/produtos/") + produto.getId());

        return ResponseEntity.created(endereco).body(produto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Produto> atualizarProduto(@PathVariable("id") Long id, @RequestBody ProdutoAtualizadoDTO dados){

        Produto produtoAtualizado = produtoService.atualizarProduto(
                id,
                dados.nome(),
                dados.descricao(),
                dados.preco(),
                dados.unidadeMedida()
        );

        return ResponseEntity.ok(produtoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProduto(@PathVariable("id") Long id){
        produtoService.deletarProduto(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/ativar")
    public ResponseEntity<Produto> ativarProduto(@PathVariable("id") Long id){
        Produto produto = produtoService.ativarProduto(id);

        return ResponseEntity.ok(produto);
    }

    @PatchMapping("/{id}/desativar")
    public ResponseEntity<Produto> desativarProduto(@PathVariable("id")Long id){
        Produto produto = produtoService.desativarProduto(id);

        return ResponseEntity.ok(produto);
    }

    @PatchMapping("/{id}/categoria")
    public ResponseEntity<Produto> alterarCategoriaProduto(@PathVariable("id")Long id, @RequestBody ProdutoCategoriaDTO dados){
        Produto produto = produtoService.alterarCategoriaProduto(id, dados.categoria());

        return ResponseEntity.ok(produto);
    }




}
