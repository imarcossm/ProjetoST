package dev.imarcossm.ProjetoPedidoST.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;

public record ProdutoRequestDTO(
        @NotBlank(message = "Nome do produto é obrigatório") String nome,
        @NotNull(message = "Valor do produto é obrigatório") @DecimalMin(value = "0.01", message = "Valor deve ser maior que zero") BigDecimal valor,
        @NotNull(message = "Estoque é obrigatório") @Min(value = 0, message = "Estoque não pode ser negativo") Integer estoque,
        @NotNull(message = "Data de cadastro é obrigatória") LocalDate dataCadastro) {
}
