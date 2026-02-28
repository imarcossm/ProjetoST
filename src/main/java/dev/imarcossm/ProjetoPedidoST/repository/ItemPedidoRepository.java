package dev.imarcossm.ProjetoPedidoST.repository;

import dev.imarcossm.ProjetoPedidoST.model.ItemPedido;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer> {

    Page<ItemPedido> findByProdutoId(Integer produtoId, Pageable pageable);
}
