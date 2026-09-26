package org.stocktrace.exception;

public class LojaNaoEncontradaException extends StockTraceException {

    public LojaNaoEncontradaException(Long id) {
        super("Loja não encontrada. ID: " + id);
    }
}