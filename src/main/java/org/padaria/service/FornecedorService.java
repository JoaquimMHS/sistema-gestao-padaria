package org.padaria.service;

import org.padaria.io.FornecedorIO;
import org.padaria.model.Fornecedor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FornecedorService implements IEntityService<Fornecedor> {

    private List<Fornecedor> fornecedores;
    private final FornecedorIO fornecedorIo;

    public FornecedorService() {
        this.fornecedores = new ArrayList<>();
        this.fornecedorIo = new FornecedorIO();
    }

    public void carregarDados(String arquivo) {
        this.fornecedores = fornecedorIo.lerCSV(arquivo);
    }

    public void salvarDados(String arquivo) {
        fornecedorIo.salvarCSV(this.fornecedores, arquivo);
    }

    @Override
    public void cadastrar(Fornecedor fornecedor) {
        if (fornecedor == null) {
            throw new IllegalArgumentException("O fornecedor não pode ser nulo.");
        }

        if (!fornecedor.isValid()) {
            throw new IllegalArgumentException("Dados do fornecedor são inválidos.");
        }

        // Verifica se já existe um fornecedor com o mesmo código
        if (this.fornecedores.stream().anyMatch(f -> f.getCodigo() == fornecedor.getCodigo())) {
            throw new IllegalArgumentException("Já existe um fornecedor com o código " + fornecedor.getCodigo() + ".");
        }

        this.fornecedores.add(fornecedor);
    }

    @Override
    public List<Fornecedor> listar() {

        return new ArrayList<>(this.fornecedores);
    }

    @Override
    public Fornecedor buscar(int codigo) {

        return this.fornecedores.stream()
                .filter(f -> f.getCodigo() == codigo)
                .findFirst()
                .orElse(null);
    }

    @Override
    public void atualizar(Fornecedor fornecedorAtualizado) {
        if (fornecedorAtualizado == null) {
            throw new IllegalArgumentException("O fornecedor para atualização não pode ser nulo.");
        }

        if (!fornecedorAtualizado.isValid()) {
            throw new IllegalArgumentException("Dados do fornecedor para atualização são inválidos.");
        }

        // busca o fornecedor
        Optional<Fornecedor> fornecedorExistenteOpt = this.fornecedores.stream()
                .filter(f -> f.getCodigo() == fornecedorAtualizado.getCodigo())
                .findFirst();

        // atualiza o fornecedor que já está na lista
        if (fornecedorExistenteOpt.isPresent()) {
            Fornecedor fornecedorExistente = fornecedorExistenteOpt.get();
            fornecedorExistente.setNome(fornecedorAtualizado.getNome());
            fornecedorExistente.setEndereco(fornecedorAtualizado.getEndereco());
            fornecedorExistente.setTelefone(fornecedorAtualizado.getTelefone());
            fornecedorExistente.setCNPJ(fornecedorAtualizado.getCNPJ());
            fornecedorExistente.setPessoaContato(fornecedorAtualizado.getPessoaContato());
        } else {
            throw new IllegalArgumentException(
                    "Fornecedor com o código " + fornecedorAtualizado.getCodigo() + " não encontrado.");
        }
    }

    @Override
    public boolean remover(int codigo) {
        return this.fornecedores.removeIf(f -> f.getCodigo() == codigo);
    }
}