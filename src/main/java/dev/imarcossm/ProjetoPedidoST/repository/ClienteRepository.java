package dev.imarcossm.ProjetoPedidoST.repository;

import dev.imarcossm.ProjetoPedidoST.model.Cliente;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ClienteRepository extends JpaRepository<Cliente, Integer> {

    @Query(value = "SELECT * FROM cliente WHERE LOWER(nome) LIKE LOWER(CONCAT('%', :nome, '%'))", countQuery = "SELECT COUNT(*) FROM cliente WHERE LOWER(nome) LIKE LOWER(CONCAT('%', :nome, '%'))", nativeQuery = true)
    Page<Cliente> findByNomeContainingIgnoreCase(@Param("nome") String nome, Pageable pageable);
}
