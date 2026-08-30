package org.example.exception;

public class ProdutoDuplicadoException extends StockTrackException {
    public ProdutoDuplicadoException(String mensagem) {
        super(mensagem);
    }
}
