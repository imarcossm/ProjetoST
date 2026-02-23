package dev.imarcossm.ProjetoPedidoST.service;

import dev.imarcossm.ProjetoPedidoST.model.Pedido;
import dev.imarcossm.ProjetoPedidoST.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository repository;

    public PedidoService(PedidoRepository repository){
        this.repository = repository;
    }

    public Pedido salvar(Pedido pedido) {
        return repository.save(pedido);
    }

    public List<Pedido> listarTodos() {
        return repository.findAll();
    }

    public Pedido buscarPeloId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
    }

    public Pedido atualizar(Integer id, Pedido pedidoAtualizado) {

        Pedido pedido = buscarPeloId(id);

        pedido.setCliente(pedidoAtualizado.getCliente());
        pedido.setDataPedido(pedidoAtualizado.getDataPedido());

        return repository.save(pedido);
    }

    public void deletar(Integer id) {
        Pedido pedido = buscarPeloId(id);
        repository.delete(pedido);
    }
}
