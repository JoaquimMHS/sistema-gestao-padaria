package org.padaria.service;

import org.padaria.io.VendaIO;
import org.padaria.model.Venda;
import org.padaria.model.ModoPagamento;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class VendaService implements IEntityService<Venda> {
    private final VendaIO vendaIO;
    private List<Venda> vendas;

    public VendaService() {
        this.vendaIO = new VendaIO();
        this.vendas = new ArrayList<>();
    }

    public void carregarDados(String arquivo) throws IOException {
        this.vendas = vendaIO.lerCSV(arquivo);
    }

    public void salvarDados(String arquivo) throws IOException {
        vendaIO.salvarCSV(this.vendas, arquivo);
    }

    @Override
    public void cadastrar(Venda venda) {
        this.vendas.add(venda);
    }

    @Override
    public Venda buscar(int codigo) {
        return vendas.stream()
                .filter(v -> v.getCodigo() == codigo)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Venda> listar() {
        return new ArrayList<>(vendas);
    }

    @Override
    public void atualizar(Venda vendaAtualizada) {
        Venda vendaExistente = buscar(vendaAtualizada.getCodigo());
        if (vendaExistente != null) {
            vendaExistente.setCodigoCliente(vendaAtualizada.getCodigoCliente());
            vendaExistente.setDataVenda(vendaAtualizada.getDataVenda());
            vendaExistente.setCodigoProduto(vendaAtualizada.getCodigoProduto());
            vendaExistente.setQuantidade(vendaAtualizada.getQuantidade());
            vendaExistente.setModoPagamento(vendaAtualizada.getModoPagamento());
        }
    }

    @Override
    public boolean remover(int codigo) {
        return vendas.removeIf(v -> v.getCodigo() == codigo);
    }

    public double calcularReceitaTotal(ProdutoService produtoService) {
        return vendas.stream()
                .mapToDouble(v -> v.calcularValorVenda(produtoService.getPrecoVenda(v.getCodigoProduto())))
                .sum();
    }

    public double calcularLucroTotal(ProdutoService produtoService) {
        return vendas.stream()
                .mapToDouble(v -> v.calcularLucro(produtoService.getPrecoVenda(v.getCodigoProduto()),
                        produtoService.getPrecoCusto(v.getCodigoProduto())))
                .sum();
    }

    public List<Venda> listarVendasPorCliente(int codigoCliente) {
        return vendas.stream()
                .filter(v -> v.getCodigoCliente() != null && v.getCodigoCliente() == codigoCliente)
                .collect(Collectors.toList());
    }

    public List<Venda> listarVendasPorProduto(int codigoProduto) {
        return vendas.stream()
                .filter(v -> v.getCodigoProduto() == codigoProduto)
                .collect(Collectors.toList());
    }

    public List<Venda> listarVendasPorPagamento(ModoPagamento modo) {
        return vendas.stream()
                .filter(v -> v.getModoPagamento() == modo)
                .collect(Collectors.toList());
    }
}