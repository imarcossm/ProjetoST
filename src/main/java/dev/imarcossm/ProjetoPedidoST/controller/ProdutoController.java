package dev.imarcossm.ProjetoPedidoST.controller;

import dev.imarcossm.ProjetoPedidoST.dto.ProdutoRequestDTO;
import dev.imarcossm.ProjetoPedidoST.dto.ProdutoResponseDTO;
import dev.imarcossm.ProjetoPedidoST.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService service;

    public ProdutoController(ProdutoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> salvar(@Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.salvar(dto));
    }

    @GetMapping
    public ResponseEntity<Page<ProdutoResponseDTO>> listar(Pageable pageable) {
        return ResponseEntity.ok(service.listarTodos(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> buscarPeloId(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPeloId(id));
    }

    @GetMapping("/buscar")
    public ResponseEntity<Page<ProdutoResponseDTO>> buscarPorNome(
            @RequestParam String nome,
            Pageable pageable) {
        return ResponseEntity.ok(service.buscarPorNome(nome, pageable));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponseDTO> atualizar(
            @PathVariable Integer id,
            @Valid @RequestBody ProdutoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Integer id) {
        service.deletar(id);
        return ResponseEntity.noContent().build();
    }
}
