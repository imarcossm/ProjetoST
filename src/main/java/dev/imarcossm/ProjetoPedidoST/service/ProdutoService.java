package dev.imarcossm.ProjetoPedidoST.service;

import dev.imarcossm.ProjetoPedidoST.dto.ProdutoRequestDTO;
import dev.imarcossm.ProjetoPedidoST.dto.ProdutoResponseDTO;
import dev.imarcossm.ProjetoPedidoST.exception.NotFoundException;
import dev.imarcossm.ProjetoPedidoST.model.Produto;
import dev.imarcossm.ProjetoPedidoST.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProdutoService {

    private final ProdutoRepository repository;

    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ProdutoResponseDTO salvar(ProdutoRequestDTO dto) {
        Produto produto = toEntity(dto);
        return toResponseDTO(repository.save(produto));
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public ProdutoResponseDTO buscarPeloId(Integer id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com id: " + id));
        return toResponseDTO(produto);
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCase(nome, pageable).map(this::toResponseDTO);
    }

    @Transactional
    public ProdutoResponseDTO atualizar(Integer id, ProdutoRequestDTO dto) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com id: " + id));
        produto.setNome(dto.nome());
        produto.setValor(dto.valor());
        produto.setEstoque(dto.estoque());
        produto.setDataCadastro(dto.dataCadastro());
        return toResponseDTO(repository.save(produto));
    }

    @Transactional
    public void deletar(Integer id) {
        Produto produto = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com id: " + id));
        repository.delete(produto);
    }

    public Produto buscarEntidadePeloId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com id: " + id));
    }

    private Produto toEntity(ProdutoRequestDTO dto) {
        Produto produto = new Produto();
        produto.setNome(dto.nome());
        produto.setValor(dto.valor());
        produto.setEstoque(dto.estoque());
        produto.setDataCadastro(dto.dataCadastro());
        return produto;
    }

    private ProdutoResponseDTO toResponseDTO(Produto produto) {
        return new ProdutoResponseDTO(
                produto.getId(),
                produto.getNome(),
                produto.getValor(),
                produto.getEstoque(),
                produto.getDataCadastro());
    }
}
