package dev.imarcossm.ProjetoPedidoST.dto;

import java.math.BigDecimal;

public record ItemPedidoResponseDTO(
        Integer id,
        Integer produtoId,
        String produtoNome,
        BigDecimal valor,
        Integer quantidade,
        BigDecimal desconto,
        BigDecimal subtotal) {
}
