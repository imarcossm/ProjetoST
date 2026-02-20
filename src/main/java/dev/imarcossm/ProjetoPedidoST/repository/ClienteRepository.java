package dev.imarcossm.ProjetoPedidoST.repository;

import dev.imarcossm.ProjetoPedidoST.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
}