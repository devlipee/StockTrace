package org.stocktrace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stocktrace.dto.MovimentacaoDTO;
import org.stocktrace.model.Movimentacao;
import org.stocktrace.service.MovimentacaoService;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/movimentacoes")
public class MovimentacaoController {

    private final MovimentacaoService movimentacaoService;

    public MovimentacaoController(MovimentacaoService movimentacaoService) {
        this.movimentacaoService = movimentacaoService;
    }

    @GetMapping
    public List<Movimentacao> listarMovimentacoes(){
        return movimentacaoService.listarMovimentacoes();
    }

    @GetMapping("/{id}")
    public Movimentacao listarMovimentacoesPorId(@PathVariable("id") Long id){
        return movimentacaoService.buscarMovimentacaoPorId(id);
    }

    @GetMapping("/estoques/{estoqueId}")
    public List<Movimentacao> listarMovimentacaoPorEstoque(@PathVariable ("estoqueId") Long estoqueId){
        return movimentacaoService.listarMovimentacoesPorEstoque(estoqueId);
    }

    @PostMapping("/entrada")
    public ResponseEntity<Movimentacao> registrarEntrada(@RequestBody MovimentacaoDTO dados){
        Movimentacao mov = movimentacaoService.registrarEntrada(
                dados.estoqueId(),
                dados.quantidade(),
                dados.motivo(),
                dados.observacao(),
                dados.responsavel()
        );

        URI endereco = URI.create("/movimentacoes/" + mov.getId());

        return ResponseEntity.created(endereco).body(mov);
    }

    @PostMapping("/saida")
    public ResponseEntity<Movimentacao>registrarSaida(@RequestBody MovimentacaoDTO dados){

        Movimentacao mov = movimentacaoService.registrarSaida(
                dados.estoqueId(),
                dados.quantidade(),
                dados.motivo(),
                dados.observacao(),
                dados.responsavel()
        );

        URI endereco = URI.create("/movimentacoes/" + mov.getId());

        return ResponseEntity.created(endereco).body(mov);

    }





}










