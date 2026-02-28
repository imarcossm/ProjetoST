package dev.imarcossm.ProjetoPedidoST.controller;

import dev.imarcossm.ProjetoPedidoST.dto.PedidoRequestDTO;
import dev.imarcossm.ProjetoPedidoST.dto.PedidoResponseDTO;
import dev.imarcossm.ProjetoPedidoST.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PedidoResponseDTO> criar(@Valid @RequestBody PedidoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<PedidoResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<PedidoResponseDTO> buscarPeloId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPeloId(id));
    }

    @GetMapping("/cliente/{clienteId}")
    public ResponseEntity<Page<PedidoResponseDTO>> listarPorCliente(
            @PathVariable Integer clienteId,
            Pageable pageable) {
        return ResponseEntity.ok(service.listarPorCliente(clienteId, pageable));
    }

    @GetMapping("/produto/{produtoId}")
    public ResponseEntity<Page<PedidoResponseDTO>> listarPorProduto(
            @PathVariable Integer produtoId,
            Pageable pageable) {
        return ResponseEntity.ok(service.listarPorProduto(produtoId, pageable));
    }

    @GetMapping("/periodo")
    public ResponseEntity<Page<PedidoResponseDTO>> listarPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dataFim,
            Pageable pageable) {
        return ResponseEntity.ok(service.listarPorPeriodo(dataInicio, dataFim, pageable));
    }

    @GetMapping("/cliente/{clienteId}/total")
    public ResponseEntity<BigDecimal> calcularTotalPorCliente(@PathVariable Integer clienteId) {
        return ResponseEntity.ok(service.calcularTotalPorCliente(clienteId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
