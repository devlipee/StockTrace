package org.stocktrace.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeExcecoes {

    @ExceptionHandler(EntidadeNaoEncontradaException.class)
    public ResponseEntity<String> tratarEntidadeNaoEncontrada(
            EntidadeNaoEncontradaException erro
    ) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(erro.getMessage());
    }

    @ExceptionHandler(DadosInvalidosException.class)
    public ResponseEntity<String> tratarDadoInvalido(
            DadosInvalidosException erro
    ){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(erro.getMessage());
    }

    @ExceptionHandler(EstoqueInsuficienteException.class)
    public ResponseEntity<String>tratarEstoqueInvalido(
            EstoqueInsuficienteException erro
    ){
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(erro.getMessage());
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> tratarIntegridadeDosDados(
            DataIntegrityViolationException erro
    ) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body("Não foi possível concluir a operação porque ela viola uma restrição dos dados.");
    }



}