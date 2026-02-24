package dev.imarcossm.ProjetoPedidoST.controller;

import dev.imarcossm.ProjetoPedidoST.model.ItemPedido;
import dev.imarcossm.ProjetoPedidoST.service.ItemPedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/itens")

public class ItemPedidoController {

    private final ItemPedidoService service;

    public ItemPedidoController(ItemPedidoService service) {
        this.service = service;
    }

    @PostMapping
    public ItemPedido salvar(@RequestBody ItemPedido item) {
        return service.salvar(item);
    }

    @GetMapping
    public List<ItemPedido> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public ItemPedido buscarPeloId(@PathVariable Integer id) {
        return service.buscarPeloId(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
