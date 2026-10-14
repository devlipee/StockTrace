package org.stocktrace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.stocktrace.dto.UsuarioDTO;
import org.stocktrace.dto.UsuarioRespostaDTO;
import org.stocktrace.service.UsuarioService;


@RestController
@RequestMapping ("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }


    @PostMapping
    public ResponseEntity<UsuarioRespostaDTO> cadastrarUsuario (@RequestBody UsuarioDTO dados){

        UsuarioRespostaDTO usuarioSalvo = usuarioService.cadastrarUsuario(
                dados.nome(),
                dados.email(),
                dados.senha()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(usuarioSalvo);

    }




}
