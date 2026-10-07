package org.stocktrace.controller;

import org.stocktrace.model.Loja;
import org.stocktrace.service.LojaService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/lojas")
public class LojaController {

    private final LojaService lojaService;

    public LojaController(LojaService lojaService){ this.lojaService = lojaService;}

    @GetMapping
    public List<Loja> listarLojas()throws SQLException, IOException{
        List<Loja> lojas = lojaService.listarLojas();
        return lojas;
    }



}
