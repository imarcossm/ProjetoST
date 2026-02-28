package dev.imarcossm.ProjetoPedidoST.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PedidoResponseDTO(
        Integer id,
        Integer clienteId,
        String clienteNome,
        LocalDate dataPedido,
        List<ItemPedidoResponseDTO> itens,
        BigDecimal total) {
}
