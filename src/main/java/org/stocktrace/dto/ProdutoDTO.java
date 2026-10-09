package org.stocktrace.dto;


import org.stocktrace.model.CategoriaProduto;

import java.math.BigDecimal;

public record ProdutoDTO(
        String codigo,
        String nome,
        String descricao,
        BigDecimal preco,
        String unidadeMedida,
        CategoriaProduto categoria
) {

}
