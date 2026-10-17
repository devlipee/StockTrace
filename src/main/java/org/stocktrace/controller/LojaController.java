package org.stocktrace.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.stocktrace.dto.LojaDTO;
import org.stocktrace.model.Loja;
import org.stocktrace.model.Usuario;
import org.stocktrace.service.LojaService;
import org.stocktrace.service.UsuarioService;

import java.io.IOException;
import java.net.URI;
import java.security.Principal;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/lojas")
public class LojaController {

    private final LojaService lojaService;
    private final UsuarioService usuarioService;

    public LojaController(
            LojaService lojaService,
            UsuarioService usuarioService
    ) {
        this.lojaService = lojaService;
        this.usuarioService = usuarioService;
    }

    @GetMapping
    public List<Loja> listarLojas(){
        List<Loja> lojas = lojaService.listarLojas();
        return lojas;
    }

    @GetMapping("/{id}")
    public Loja buscarLojaPorId(@PathVariable("id") Long id){
        return lojaService.buscarLojaPorId(id);
    }

    @PostMapping
    public ResponseEntity<Loja> cadastrarLoja(
            @RequestBody LojaDTO dados,
            Principal principal
    ) {
        Usuario usuario = usuarioService.buscarUsuarioPorEmail(
                principal.getName()
        );

        Loja loja = lojaService.cadastrarLoja(
                usuario,
                dados.nome(),
                dados.cidade(),
                dados.bairro(),
                dados.rua(),
                dados.numero(),
                dados.complemento(),
                dados.cep()
        );

        URI endereco = URI.create("/lojas/" + loja.getId());

        return ResponseEntity.created(endereco).body(loja);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Loja> atualizarLoja(@PathVariable ("id")Long id, @RequestBody LojaDTO dados){

       Loja lojaAtualizada = lojaService.atualizarLoja(
                id,
                dados.nome(),
                dados.cidade(),
                dados.bairro(),
                dados.rua(),
                dados.numero(),
                dados.complemento(),
                dados.cep()
        );
       return ResponseEntity.ok(lojaAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarLoja(@PathVariable("id")Long id){
        lojaService.deletarLoja(id);
        return ResponseEntity.noContent().build();
    }


}
