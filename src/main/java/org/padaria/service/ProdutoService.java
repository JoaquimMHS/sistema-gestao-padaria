package org.padaria.service;

import java.util.List;
import java.util.stream.Collectors;
import org.padaria.io.ProdutoIO;
import org.padaria.model.Produto;

public class ProdutoService implements IEntityService<Produto> {

    private List<Produto> produtos;

    public ProdutoService() {
        this.produtos = new java.util.ArrayList<>();
    }

    public void carregarDados(String arquivo) {
        this.produtos = new ProdutoIO().lerCSV(arquivo); 
        System.out.println(this.produtos.size() + " produtos carregados com sucesso.");
    }

    public void salvarDados(String arquivo) {
        new ProdutoIO().salvarCSV(this.produtos, arquivo);
        System.out.println("Produtos salvos com sucesso em " + arquivo);
    }

    public boolean verificarEstoqueBaixo(Produto produto) {
        return produto.getEstoqueAtual() < produto.getEstoqueMinimo();
    }

    public List<Produto> listarEstoqueBaixo() {
        return this.produtos.stream()
                .filter(this::verificarEstoqueBaixo)
                .collect(Collectors.toList());
    }

    public boolean atualizarEstoque(int codigo, int quantidade) {
        Produto produto = this.buscar(codigo);
        if (produto != null) {
            int novoEstoque = produto.getEstoqueAtual() + quantidade;
            produto.setEstoqueAtual(novoEstoque);
            return true;
        }
        return false; 
    }

    @Override
    public void cadastrar(Produto produto) {
        if (buscar(produto.getCodigo()) == null) {
            this.produtos.add(produto);
        } else {
            System.out.println("Erro: Já existe um produto com o código " + produto.getCodigo());
        }
    }

    @Override
    public Produto buscar(int id) {
        return this.produtos.stream()
                .filter(p -> p.getCodigo() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Produto> listar() {
        return this.produtos;
    }

    @Override
    public void atualizar(Produto produtoParaAtualizar) {
        Produto produtoExistente = buscar(produtoParaAtualizar.getCodigo());
        if (produtoExistente != null) {
            produtoExistente.setDescricao(produtoParaAtualizar.getDescricao());
            produtoExistente.setEstoqueMinimo(produtoParaAtualizar.getEstoqueMinimo());
            produtoExistente.setEstoqueAtual(produtoParaAtualizar.getEstoqueAtual());
            produtoExistente.setValorCusto(produtoParaAtualizar.getValorCusto());
            produtoExistente.setPercentualLucro(produtoParaAtualizar.getPercentualLucro());
        }
    }

    @Override
    public boolean remover(int id) {
        return this.produtos.removeIf(p -> p.getCodigo() == id);
    }
}