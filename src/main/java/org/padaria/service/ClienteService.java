package org.padaria.service;

import org.padaria.model.Cliente;
import org.padaria.model.IEntity;
import org.padaria.io.ClienteIO;

import java.util.ArrayList;
import java.util.List;

public class ClienteService implements IEntityService<Cliente> {
    
    private List<Cliente> clientes;
    private ClienteIO clienteIO;
    private String caminhoArquivo;

    public ClienteService(String caminhoArquivo) {
        this.clientes = new ArrayList<>();
        this.clienteIO = new ClienteIO();
        this.caminhoArquivo = caminhoArquivo;
        carregarClientes();
    }

    private void carregarClientes() {
        try {
            this.clientes = clienteIO.lerCSV(this.caminhoArquivo);
        } catch (Exception e) {
            System.err.println("Erro ao carregar clientes do arquivo: " + e.getMessage());
        }
    }

    private void salvarClientes() {
        try {
            clienteIO.salvarCSV(this.clientes, this.caminhoArquivo);
        } catch (Exception e) {
            System.err.println("Erro ao salvar clientes no arquivo: " + e.getMessage());
        }
    }

    @Override
    public void cadastrar(Cliente entidade) {
        if (entidade != null && entidade.isValid()) {
            this.clientes.add(entidade);
            salvarClientes();
        }
    }

    @Override
    public Cliente buscar(int codigo) {
        for (Cliente cliente : this.clientes) {
            if (cliente.getCodigo() == codigo) {
                return cliente;
            }
        }
        return null;
    }

    @Override
    public List<Cliente> listar() {
        return new ArrayList<>(this.clientes);
    }

    @Override
    public void atualizar(Cliente entidade) {
        if (entidade != null && entidade.isValid()) {
            for (int i = 0; i < this.clientes.size(); i++) {
                if (this.clientes.get(i).getCodigo() == entidade.getCodigo()) {
                    this.clientes.set(i, entidade);
                    salvarClientes();
                    return;
                }
            }
        }
    }

    @Override
    public boolean remover(int codigo) {
        boolean removido = this.clientes.removeIf(cliente -> cliente.getCodigo() == codigo);
        if (removido) {
            salvarClientes();
        }
        return removido;
    }
}