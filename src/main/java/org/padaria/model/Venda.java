package org.padaria.model;

import java.time.LocalDate;

public class Venda implements IEntity {
    private int codigoVenda;
    private Integer codigoCliente;
    private LocalDate dataVenda;
    private int codigoProduto;
    private int quantidade;
    private ModoPagamento modoPagamento;

    public Venda() {
    }

    public Venda(Integer codigoCliente, LocalDate dataVenda, int codigoProduto, int quantidade,
            ModoPagamento modoPagamento) {
        this.codigoVenda = codigoVenda;
        this.codigoCliente = codigoCliente;
        this.dataVenda = dataVenda;
        this.codigoProduto = codigoProduto;
        this.quantidade = quantidade;
        this.modoPagamento = modoPagamento;
    }

    // Getters
    @Override
    public int getCodigo() {
        return codigoVenda;
    }

    public Integer getCodigoCliente() {
        return codigoCliente;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public int getCodigoProduto() {
        return codigoProduto;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public ModoPagamento getModoPagamento() {
        return modoPagamento;
    }

    // Setters
    @Override
    public void setCodigo(int codigo) {
        this.codigoVenda = codigo;
    }

    public void setCodigoCliente(Integer codigoCliente) {
        this.codigoCliente = codigoCliente;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
    }

    public void setCodigoProduto(int codigoProduto) {
        this.codigoProduto = codigoProduto;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setModoPagamento(ModoPagamento modoPagamento) {
        this.modoPagamento = modoPagamento;
    }

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
        return "Venda" +
                "codigoCliente=" + (codigoCliente != null ? codigoCliente : "N/A") +
                ", dataVenda=" + dataVenda +
                ", codigoProduto=" + codigoProduto +
                ", quantidade=" + quantidade +
                ", modoPagamento=" + modoPagamento;
    }
}