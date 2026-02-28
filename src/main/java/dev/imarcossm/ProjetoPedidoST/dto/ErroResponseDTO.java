package dev.imarcossm.ProjetoPedidoST.dto;

import java.time.LocalDateTime;
import java.util.List;

public record ErroResponseDTO(
        int status,
        String mensagem,
        LocalDateTime timestamp,
        List<String> erros) {
    public ErroResponseDTO(int status, String mensagem, LocalDateTime timestamp) {
        this(status, mensagem, timestamp, null);
    }
}
