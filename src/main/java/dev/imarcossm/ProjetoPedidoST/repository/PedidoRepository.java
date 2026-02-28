package dev.imarcossm.ProjetoPedidoST.repository;

import dev.imarcossm.ProjetoPedidoST.model.Pedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    @Query(value = "SELECT * FROM pedido WHERE id_cliente = :clienteId", countQuery = "SELECT COUNT(*) FROM pedido WHERE id_cliente = :clienteId", nativeQuery = true)
    Page<Pedido> findByClienteId(@Param("clienteId") Integer clienteId, Pageable pageable);

    @Query(value = "SELECT DISTINCT p.* FROM pedido p " +
            "JOIN item_pedido ip ON p.id = ip.id_pedido " +
            "WHERE ip.id_produto = :produtoId", countQuery = "SELECT COUNT(DISTINCT p.id) FROM pedido p " +
                    "JOIN item_pedido ip ON p.id = ip.id_pedido " +
                    "WHERE ip.id_produto = :produtoId", nativeQuery = true)
    Page<Pedido> findByProdutoId(@Param("produtoId") Integer produtoId, Pageable pageable);

    @Query(value = "SELECT * FROM pedido WHERE data_pedido BETWEEN :dataInicio AND :dataFim", countQuery = "SELECT COUNT(*) FROM pedido WHERE data_pedido BETWEEN :dataInicio AND :dataFim", nativeQuery = true)
    Page<Pedido> findByDataPedidoBetween(
            @Param("dataInicio") LocalDate dataInicio,
            @Param("dataFim") LocalDate dataFim,
            Pageable pageable);

    @Query(value = "SELECT COALESCE(SUM(ip.valor * ip.quantidade - COALESCE(ip.desconto, 0)), 0) " +
            "FROM pedido p JOIN item_pedido ip ON p.id = ip.id_pedido " +
            "WHERE p.id_cliente = :clienteId", nativeQuery = true)
    BigDecimal calcularTotalPorCliente(@Param("clienteId") Integer clienteId);
}
