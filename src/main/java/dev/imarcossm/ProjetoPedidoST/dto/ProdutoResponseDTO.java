package dev.imarcossm.ProjetoPedidoST.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoResponseDTO(
        Integer id,
        String nome,
        BigDecimal valor,
        Integer estoque,
        LocalDate dataCadastro) {
}
