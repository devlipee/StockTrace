package org.stocktrace.dto;

public record LojaDTO(
        String nome,
        String cidade,
        String bairro,
        String rua,
        String numero,
        String complemento,
        String cep
) {

}