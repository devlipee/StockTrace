package org.example.exception;

public class EstoqueInsuficienteException extends StockTrackException {
    public EstoqueInsuficienteException(String mensagem) {
        super(mensagem);
    }
}
