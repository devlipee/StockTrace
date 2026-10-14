package org.stocktrace.dto;

public record UsuarioRespostaDTO (
        Long id,
        String nome,
        String email,
        boolean ativo
) {
}
