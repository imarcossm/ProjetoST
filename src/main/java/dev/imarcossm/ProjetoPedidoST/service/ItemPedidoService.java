package dev.imarcossm.ProjetoPedidoST.service;

import dev.imarcossm.ProjetoPedidoST.model.ItemPedido;
import dev.imarcossm.ProjetoPedidoST.model.Produto;
import dev.imarcossm.ProjetoPedidoST.repository.ItemPedidoRepository;
import dev.imarcossm.ProjetoPedidoST.repository.ProdutoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ItemPedidoService {

    private final ItemPedidoRepository repository;
    private final ProdutoRepository produtoRepository;

    public ItemPedidoService(ItemPedidoRepository repository,
                             ProdutoRepository produtoRepository) {
        this.repository = repository;
        this.produtoRepository = produtoRepository;
    }

    public ItemPedido salvar(ItemPedido item) {

        Produto produto = produtoRepository.findById(
                item.getProduto().getId()
        ).orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (produto.getEstoque() < item.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente");
        }

        item.setValor(produto.getValor());

        produto.setEstoque(
                produto.getEstoque() - item.getQuantidade()
        );

        produtoRepository.save(produto);

        return repository.save(item);
    }

    public List<ItemPedido> listarTodos() {
        return repository.findAll();
    }

    public ItemPedido buscarPeloId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
    }

    public void deletar(Integer id) {
        ItemPedido item = buscarPeloId(id);
        repository.delete(item);
    }
}