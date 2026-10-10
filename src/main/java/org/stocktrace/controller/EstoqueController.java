package org.stocktrace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stocktrace.dto.EstoqueDTO;
import org.stocktrace.model.Estoque;
import org.stocktrace.service.EstoqueService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/estoques")
public class EstoqueController {

    private final EstoqueService estoqueService;

    public EstoqueController(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    @GetMapping
    public List<Estoque> listarEstoque(){
        return estoqueService.listarEstoques();
    }

    @GetMapping("/{id}")
    public Estoque buscarEstoquePorId(@PathVariable("id") Long id){
        return estoqueService.buscarEstoquePorId(id);
    }

    @PostMapping
    public ResponseEntity<Estoque> cadastrarEstoque (@RequestBody  EstoqueDTO dados){

        Estoque estoque = estoqueService.cadastrarEstoque(
                dados.produtoId(),
                dados.lojaId()
        );

        URI endereco = URI.create("/estoques/"+ estoque.getId());

        return ResponseEntity.created(endereco).body(estoque);
    }

    @GetMapping("/produto/{produtoId}/loja/{lojaId}")
    public ResponseEntity<Estoque> buscarEstoquePorProdutoELoja(
            @PathVariable("produtoId") Long produtoId,
            @PathVariable("lojaId") Long lojaId
    ) {
        Estoque estoque = estoqueService.buscarEstoquePorProdutoELoja(
                produtoId,
                lojaId
        );

        if (estoque == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(estoque);
    }





}
