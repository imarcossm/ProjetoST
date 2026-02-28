package dev.imarcossm.ProjetoPedidoST.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ItemPedidoRequestDTO(
        @NotNull(message = "ID do produto é obrigatório") Integer produtoId,
        @NotNull(message = "Valor do item é obrigatório") @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero") BigDecimal valor,
        @NotNull(message = "Quantidade é obrigatória") @Min(value = 1, message = "Quantidade deve ser no mínimo 1") Integer quantidade,
        BigDecimal desconto) {
}
