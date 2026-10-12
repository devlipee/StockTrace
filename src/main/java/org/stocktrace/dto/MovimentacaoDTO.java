package org.stocktrace.dto;

public record MovimentacaoDTO(
        Long estoqueId,
        int quantidade,
        String motivo,
        String observacao,
        String responsavel
) {
}
