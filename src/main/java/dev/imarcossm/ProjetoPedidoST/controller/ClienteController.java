package dev.imarcossm.ProjetoPedidoST.controller;

import dev.imarcossm.ProjetoPedidoST.model.Cliente;
import dev.imarcossm.ProjetoPedidoST.service.ClienteService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/clientes")

public class ClienteController {

    private final ClienteService service;

    public ClienteController(ClienteService service) {
        this.service = service;
    }

    @PostMapping
    public Cliente salvar(@RequestBody Cliente cliente) {
        return service.salvar(cliente);
    }

    @GetMapping
    public List<Cliente> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Cliente buscarPeloId(@PathVariable Integer id) {
        return service.buscarPeloId(id);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Integer id, @RequestBody Cliente cliente) {
        return service.atualizar(id, cliente);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
