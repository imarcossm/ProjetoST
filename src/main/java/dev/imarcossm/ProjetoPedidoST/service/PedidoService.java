package dev.imarcossm.ProjetoPedidoST.service;

import dev.imarcossm.ProjetoPedidoST.dto.ItemPedidoRequestDTO;
import dev.imarcossm.ProjetoPedidoST.dto.ItemPedidoResponseDTO;
import dev.imarcossm.ProjetoPedidoST.dto.PedidoRequestDTO;
import dev.imarcossm.ProjetoPedidoST.dto.PedidoResponseDTO;
import dev.imarcossm.ProjetoPedidoST.exception.BusinessException;
import dev.imarcossm.ProjetoPedidoST.exception.NotFoundException;
import dev.imarcossm.ProjetoPedidoST.model.Cliente;
import dev.imarcossm.ProjetoPedidoST.model.ItemPedido;
import dev.imarcossm.ProjetoPedidoST.model.Pedido;
import dev.imarcossm.ProjetoPedidoST.model.Produto;
import dev.imarcossm.ProjetoPedidoST.repository.PedidoRepository;
import dev.imarcossm.ProjetoPedidoST.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ClienteService clienteService;
    private final ProdutoRepository produtoRepository;

    public PedidoService(PedidoRepository pedidoRepository,
            ClienteService clienteService,
            ProdutoRepository produtoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.clienteService = clienteService;
        this.produtoRepository = produtoRepository;
    }

    @Transactional
    public PedidoResponseDTO criar(PedidoRequestDTO dto) {
        Cliente cliente = clienteService.buscarEntidadePeloId(dto.clienteId());

        if (dto.itens() == null || dto.itens().isEmpty()) {
            throw new BusinessException("Pedido deve conter ao menos um produto");
        }

        Pedido pedido = new Pedido();
        pedido.setCliente(cliente);
        pedido.setDataPedido(dto.dataPedido() != null ? dto.dataPedido() : LocalDate.now());

        List<ItemPedido> itens = new ArrayList<>();
        for (ItemPedidoRequestDTO itemDTO : dto.itens()) {
            Produto produto = produtoRepository.findById(itemDTO.produtoId())
                    .orElseThrow(
                            () -> new NotFoundException("Produto não encontrado com id: " + itemDTO.produtoId()));

            int novoEstoque = produto.getEstoque() - itemDTO.quantidade();
            if (novoEstoque < 0) {
                throw new BusinessException(
                        "Estoque insuficiente para o produto '" + produto.getNome() +
                                "'. Disponível: " + produto.getEstoque() +
                                ", solicitado: " + itemDTO.quantidade());
            }

            produto.setEstoque(novoEstoque);
            produtoRepository.save(produto);

            ItemPedido item = new ItemPedido();
            item.setPedido(pedido);
            item.setProduto(produto);
            item.setValor(itemDTO.valor());
            item.setQuantidade(itemDTO.quantidade());
            item.setDesconto(itemDTO.desconto());
            itens.add(item);
        }

        pedido.setItens(itens);
        Pedido salvo = pedidoRepository.save(pedido);
        return toResponseDTO(salvo);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listarTodos(Pageable pageable) {
        return pedidoRepository.findAll(pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public PedidoResponseDTO buscarPeloId(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado com id: " + id));
        return toResponseDTO(pedido);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listarPorCliente(Integer clienteId, Pageable pageable) {
        clienteService.buscarEntidadePeloId(clienteId);
        return pedidoRepository.findByClienteId(clienteId, pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listarPorProduto(Integer produtoId, Pageable pageable) {
        produtoRepository.findById(produtoId)
                .orElseThrow(() -> new NotFoundException("Produto não encontrado com id: " + produtoId));
        return pedidoRepository.findByProdutoId(produtoId, pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public Page<PedidoResponseDTO> listarPorPeriodo(LocalDate dataInicio, LocalDate dataFim, Pageable pageable) {
        if (dataInicio == null || dataFim == null) {
            throw new BusinessException("Data inicial e data final são obrigatórias");
        }
        if (dataInicio.isAfter(dataFim)) {
            throw new BusinessException("Data inicial não pode ser posterior à data final");
        }
        return pedidoRepository.findByDataPedidoBetween(dataInicio, dataFim, pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public BigDecimal calcularTotalPorCliente(Integer clienteId) {
        clienteService.buscarEntidadePeloId(clienteId);
        BigDecimal total = pedidoRepository.calcularTotalPorCliente(clienteId);
        return total != null ? total : BigDecimal.ZERO;
    }

    @Transactional
    public void deletar(Integer id) {
        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Pedido não encontrado com id: " + id));
        pedidoRepository.delete(pedido);
    }

    private PedidoResponseDTO toResponseDTO(Pedido pedido) {
        List<ItemPedidoResponseDTO> itensDTO = pedido.getItens() == null
                ? new ArrayList<>()
                : pedido.getItens().stream()
                        .map(this::toItemResponseDTO)
                        .collect(Collectors.toList());

        return new PedidoResponseDTO(
                pedido.getId(),
                pedido.getCliente().getId(),
                pedido.getCliente().getNome(),
                pedido.getDataPedido(),
                itensDTO,
                pedido.getTotal());
    }

    private ItemPedidoResponseDTO toItemResponseDTO(ItemPedido item) {
        return new ItemPedidoResponseDTO(
                item.getId(),
                item.getProduto().getId(),
                item.getProduto().getNome(),
                item.getValor(),
                item.getQuantidade(),
                item.getDesconto(),
                item.getSubtotal());
    }
}
