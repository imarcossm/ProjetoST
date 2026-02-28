package dev.imarcossm.ProjetoPedidoST.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public record PedidoRequestDTO(
        @NotNull(message = "ID do cliente é obrigatório") Integer clienteId,
        @NotNull(message = "Data do pedido é obrigatória") LocalDate dataPedido,
        @NotNull(message = "Itens do pedido são obrigatórios") @NotEmpty(message = "Pedido deve conter ao menos um produto") @Valid List<ItemPedidoRequestDTO> itens) {
}
