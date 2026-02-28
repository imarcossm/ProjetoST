package dev.imarcossm.ProjetoPedidoST.dto;

import java.time.LocalDate;

public record ClienteResponseDTO(
        Integer id,
        String nome,
        String email,
        LocalDate dataCadastro) {
}
