// src/main/java/org/padaria/service/CompraService.java
package org.padaria.service;

import org.padaria.io.CompraIO;
import org.padaria.model.Compra;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CompraService implements IEntityService<Compra> {
    private final CompraIO compraIO;
    private List<Compra> compras;

    public CompraService() {
        this.compraIO = new CompraIO();
        this.compras = new ArrayList<>();
    }

    public void carregarDados(String arquivo) throws IOException {
        this.compras = compraIO.lerCSV(arquivo);
    }

    public void salvarDados(String arquivo) throws IOException {
        compraIO.salvarCSV(this.compras, arquivo);
    }

    @Override
    public void cadastrar(Compra compra) {
        this.compras.add(compra);
    }

    @Override
    public Compra buscar(int numeroNF) {
        return compras.stream()
                .filter(c -> c.getNumeroNotaFiscal() == numeroNF)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Compra> listar() {
        return new ArrayList<>(compras);
    }

    @Override
    public void atualizar(Compra compraAtualizada) {
        Compra compraExistente = buscar(compraAtualizada.getNumeroNotaFiscal());
        if (compraExistente != null) {
            compraExistente.setCodigoFornecedor(compraAtualizada.getCodigoFornecedor());
            compraExistente.setDataCompra(compraAtualizada.getDataCompra());
            compraExistente.setCodigoProduto(compraAtualizada.getCodigoProduto());
            compraExistente.setQuantidade(compraAtualizada.getQuantidade());
        }
    }

    @Override
    public boolean remover(int numeroNF) {
        compras.removeIf(c -> c.getNumeroNotaFiscal() == numeroNF);
        return false;
    }

    public double calcularGastoTotal(ProdutoService produtoService) {
        return compras.stream()
                .mapToDouble(c -> c.calcularValorCompra(produtoService.getPrecoCusto(c.getCodigoProduto())))
                .sum();
    }

    public List<Compra> listarComprasPorFornecedor(int codigoFornecedor) {
        return compras.stream()
                .filter(c -> c.getCodigoFornecedor() == codigoFornecedor)
                .collect(Collectors.toList());
    }

    public void atualizarEstoqueProduto(Compra compra) {
        System.out.println("Atualizando estoque do produto " + compra.getCodigoProduto() + " com " + compra.getQuantidade() + " unidades.");
    }
}