package org.padaria.model;

import java.time.LocalDate;

public class Venda implements IEntity {
    private int codigo;
    private Integer codigoCliente;
    private LocalDate dataVenda;
    private int codigoProduto;
    private int quantidade;
    private ModoPagamento modoPagamento;

    public Venda() {}

    public Venda(int codigo, Integer codigoCliente, LocalDate dataVenda, int codigoProduto, int quantidade, ModoPagamento modoPagamento) {
        this.codigo = codigo;
        this.codigoCliente = codigoCliente;
        this.dataVenda = dataVenda;
        this.codigoProduto = codigoProduto;
        this.quantidade = quantidade;
        this.modoPagamento = modoPagamento;
    }

    // Getters
    public int getCodigo() { return codigo; }
    public Integer getCodigoCliente() { return codigoCliente; }
    public LocalDate getDataVenda() { return dataVenda; }
    public int getCodigoProduto() { return codigoProduto; }
    public int getQuantidade() { return quantidade; }
    public ModoPagamento getModoPagamento() { return modoPagamento; }

    // Setters
    public void setCodigo(int codigo) { this.codigo = codigo; }
    public void setCodigoCliente(Integer codigoCliente) { this.codigoCliente = codigoCliente; }
    public void setDataVenda(LocalDate dataVenda) { this.dataVenda = dataVenda; }
    public void setCodigoProduto(int codigoProduto) { this.codigoProduto = codigoProduto; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }
    public void setModoPagamento(ModoPagamento modoPagamento) { this.modoPagamento = modoPagamento; }

    public double calcularValorVenda(double valorUnitario) {
        return this.quantidade * valorUnitario;
    }

    public double calcularLucro(double valorUnitario, double valorCusto) {
        return (valorUnitario - valorCusto) * this.quantidade;
    }

    public boolean isValid() {
        return codigoProduto > 0 && quantidade > 0 && modoPagamento != null;
    }

    @Override
    public String toString() {
        return "Venda{" +
                "codigo=" + codigo +
                ", codigoCliente=" + (codigoCliente != null ? codigoCliente : "N/A") +
                ", dataVenda=" + dataVenda +
                ", codigoProduto=" + codigoProduto +
                ", quantidade=" + quantidade +
                ", modoPagamento=" + modoPagamento +
                '}';
    }
}