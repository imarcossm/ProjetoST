package dev.imarcossm.ProjetoPedidoST.controller;

import dev.imarcossm.ProjetoPedidoST.dto.ClienteRequestDTO;
import dev.imarcossm.ProjetoPedidoST.dto.ClienteResponseDTO;
import dev.imarcossm.ProjetoPedidoST.service.ClienteService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> salvar(@Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ClienteResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> buscarPeloId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPeloId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ClienteResponseDTO>> buscarPorNome(
            @RequestParam String nome,
            Pageable pageable) {
        return ResponseEntity.ok(service.buscarPorNome(nome, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ClienteRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
