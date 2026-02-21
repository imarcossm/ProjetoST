package dev.imarcossm.ProjetoPedidoST.service;

import dev.imarcossm.ProjetoPedidoST.model.Produto;
import dev.imarcossm.ProjetoPedidoST.repository.ProdutoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public Produto salvar(Produto produto) {
        return repository.save(produto);
    }

    public List<Produto> listarTodos() {
        return repository.findAll();
    }

    public Produto buscarPeloId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
    }

    public Produto atualizar(Integer id, Produto produtoAtualizado) {

        Produto produto = buscarPeloId(id);

        produto.setNome(produtoAtualizado.getNome());
        produto.setValor(produtoAtualizado.getValor());
        produto.setEstoque(produtoAtualizado.getEstoque());
        produto.setDataCadastro(produtoAtualizado.getDataCadastro());

        return repository.save(produto);
    }

    public void deletar(Integer id) {
        Produto produto = buscarPeloId(id);
        repository.delete(produto);
    }
}
