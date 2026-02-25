package dev.imarcossm.ProjetoPedidoST.service;

import dev.imarcossm.ProjetoPedidoST.model.Cliente;
import dev.imarcossm.ProjetoPedidoST.repository.ClienteRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClienteService {

    private final ClienteRepository repository;

    public ClienteService(ClienteRepository repository) {
        this.repository = repository;
    }

    public Cliente salvar(Cliente cliente) {
        return repository.save(cliente);
    }

    public Page<Cliente> listarTodos(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public Cliente buscarPeloId(Integer id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));
    }

    public Cliente atualizar(Integer id, Cliente clienteAtualizado) {
        Cliente cliente = buscarPeloId(id);
        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setDataCadastro(clienteAtualizado.getDataCadastro());
        return repository.save(cliente);
    }

    public void deletar(Integer id) {
        Cliente cliente = buscarPeloId(id);
        repository.delete(cliente);
    }
}