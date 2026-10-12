package org.stocktrace.dto;

import java.math.BigDecimal;

public record ProdutoAtualizadoDTO(
        String nome,
        String descricao ,
        BigDecimal preco,
        String unidadeMedida
) {
}
