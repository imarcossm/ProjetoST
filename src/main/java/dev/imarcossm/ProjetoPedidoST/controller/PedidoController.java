package dev.imarcossm.ProjetoPedidoST.controller;

import dev.imarcossm.ProjetoPedidoST.model.Pedido;
import dev.imarcossm.ProjetoPedidoST.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos")

public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service){
        this.service = service;
    }

    @PostMapping
    public Pedido salvar(@RequestBody Pedido pedido) {
       return service.salvar(pedido);
   }

    @GetMapping
    public List<Pedido> listar() {
        return service.listarTodos();
    }

    @GetMapping("/{id}")
    public Pedido buscarPeloId(@PathVariable Integer id) {
        return service.buscarPeloId(id);
    }

    @PutMapping("/{id}")
    public Pedido atualizar(@PathVariable Integer id, @RequestBody Pedido pedido) {
        return service.atualizar(id, pedido);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        service.deletar(id);
    }
}
