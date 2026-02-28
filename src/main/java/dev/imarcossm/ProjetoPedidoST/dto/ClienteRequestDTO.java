package dev.imarcossm.ProjetoPedidoST.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

public record ClienteRequestDTO(
        @NotBlank(message = "Nome é obrigatório") String nome,
        @NotBlank(message = "Email é obrigatório") @Email(message = "Email inválido") String email,
        @NotNull(message = "Data de cadastro é obrigatória") LocalDate dataCadastro) {
}
