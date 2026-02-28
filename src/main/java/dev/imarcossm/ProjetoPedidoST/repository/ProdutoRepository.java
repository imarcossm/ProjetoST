package dev.imarcossm.ProjetoPedidoST.repository;

import dev.imarcossm.ProjetoPedidoST.model.Produto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

    @Query(value = "SELECT * FROM produto WHERE LOWER(nome) LIKE LOWER(CONCAT('%', :nome, '%'))", countQuery = "SELECT COUNT(*) FROM produto WHERE LOWER(nome) LIKE LOWER(CONCAT('%', :nome, '%'))", nativeQuery = true)
    Page<Produto> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
}
