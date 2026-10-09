package org.stocktrace.controller;

import org.springframework.web.bind.annotation.*;
import org.stocktrace.model.Loja;
import org.stocktrace.service.LojaService;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/lojas")
public class LojaController {

    private final LojaService lojaService;

    public LojaController(LojaService lojaService){ this.lojaService = lojaService;}

    @GetMapping
    public List<Loja> listarLojas(){
        List<Loja> lojas = lojaService.listarLojas();
        return lojas;
    }

    @GetMapping("/{id}")
    public Loja buscarLojaPorId(@PathVariable("id") Long id){
        return lojaService.buscarLojaPorId(id);
    }



}
