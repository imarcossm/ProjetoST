package dev.imarcossm.ProjetoPedidoST.service;

import dev.imarcossm.ProjetoPedidoST.dto.ClienteRequestDTO;
import dev.imarcossm.ProjetoPedidoST.dto.ClienteResponseDTO;
import dev.imarcossm.ProjetoPedidoST.exception.NotFoundException;
import dev.imarcossm.ProjetoPedidoST.model.Cliente;
import dev.imarcossm.ProjetoPedidoST.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public ClienteResponseDTO salvar(ClienteRequestDTO dto) {
        Cliente cliente = toEntity(dto);
        return toResponseDTO(repository.save(cliente));
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> listarTodos(Pageable pageable) {
        return repository.findAll(pageable).map(this::toResponseDTO);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarPeloId(Integer id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com id: " + id));
        return toResponseDTO(cliente);
    }

    @Transactional(readOnly = true)
    public Page<ClienteResponseDTO> buscarPorNome(String nome, Pageable pageable) {
        return repository.findByNomeContainingIgnoreCase(nome, pageable).map(this::toResponseDTO);
    }

    @Transactional
    public ClienteResponseDTO atualizar(Integer id, ClienteRequestDTO dto) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com id: " + id));
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setDataCadastro(dto.dataCadastro());
        return toResponseDTO(repository.save(cliente));
    }

    @Transactional
    public void deletar(Integer id) {
        Cliente cliente = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com id: " + id));
        repository.delete(cliente);
    }

    public Cliente buscarEntidadePeloId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado com id: " + id));
    }

    private Cliente toEntity(ClienteRequestDTO dto) {
        Cliente cliente = new Cliente();
        cliente.setNome(dto.nome());
        cliente.setEmail(dto.email());
        cliente.setDataCadastro(dto.dataCadastro());
        return cliente;
    }

    private ClienteResponseDTO toResponseDTO(Cliente cliente) {
        return new ClienteResponseDTO(
                cliente.getId(),
                cliente.getNome(),
                cliente.getEmail(),
                cliente.getDataCadastro());
    }
}
