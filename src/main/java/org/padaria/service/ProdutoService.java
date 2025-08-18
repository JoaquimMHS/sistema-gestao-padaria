// src/main/java/org/padaria/service/ProdutoService.java
package org.padaria.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.padaria.io.ProdutoIO;
import org.padaria.model.Produto;
import org.padaria.util.IOExceptionHandler;

public class ProdutoService implements IEntityService<Produto> {

    private List<Produto> produtos;
    private final ProdutoIO produtoIO;
    private static int proximoCodigo = 1;

    public ProdutoService() {
        this.produtos = new ArrayList<>();
        this.produtoIO = new ProdutoIO();
    }

    public void carregarDados(String arquivo) throws IOExceptionHandler {
        this.produtos = produtoIO.lerCSV(arquivo);
        System.out.println(this.produtos.size() + " produtos carregados com sucesso.");
    }

    public void salvarDados(String arquivo) throws IOExceptionHandler {
        produtoIO.salvarCSV(this.produtos, arquivo);
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
            produto.setCodigo(proximoCodigo++);
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
        this.produtos.removeIf(p -> p.getCodigo() == id);
        return false;
    }

    // Métodos para obtenção de dados do produto
    public double getPrecoVenda(int codigoProduto) {
        Produto produto = buscar(codigoProduto);
        return produto != null ? produto.calcularValorVenda() : 0.0;
    }

    public double getPrecoCusto(int codigoProduto) {
        Produto produto = buscar(codigoProduto);
        return produto != null ? produto.getValorCusto() : 0.0;
    }

    public String getDescricao(int codigoProduto) {
        Produto produto = buscar(codigoProduto);
        return produto != null ? produto.getDescricao() : "Produto Desconhecido";
    }
}