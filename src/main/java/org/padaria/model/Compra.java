// src/main/java/org/padaria/model/Compra.java
package org.padaria.model;

import java.time.LocalDate;

public class Compra implements IEntity {
    private int numeroNotaFiscal;
    private int codigoFornecedor;
    private LocalDate dataCompra;
    private int codigoProduto;
    private int quantidade;

    public Compra() {}

    public Compra(int numeroNotaFiscal, int codigoFornecedor, LocalDate dataCompra, int codigoProduto, int quantidade) {
        this.numeroNotaFiscal = numeroNotaFiscal;
        this.codigoFornecedor = codigoFornecedor;
        this.dataCompra = dataCompra;
        this.codigoProduto = codigoProduto;
        this.quantidade = quantidade;
    }

    // Getters
    public int getNumeroNotaFiscal() { return numeroNotaFiscal; }
    public void setNumeroNotaFiscal(int numeroNotaFiscal) { this.numeroNotaFiscal = numeroNotaFiscal; }
    public int getCodigoFornecedor() { return codigoFornecedor; }
    public void setCodigoFornecedor(int codigoFornecedor) { this.codigoFornecedor = codigoFornecedor; }
    public LocalDate getDataCompra() { return dataCompra; }
    public void setDataCompra(LocalDate dataCompra) { this.dataCompra = dataCompra; }
    public int getCodigoProduto() { return codigoProduto; }
    public void setCodigoProduto(int codigoProduto) { this.codigoProduto = codigoProduto; }
    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public double calcularValorCompra(double valorCustoUnitario) {
        return this.quantidade * valorCustoUnitario;
    }

    @Override
    public int getCodigo() {
        return 0;
    }

    @Override
    public void setCodigo(int codigo) {

    }

    public boolean isValid() {
        return numeroNotaFiscal > 0 && codigoFornecedor > 0 && codigoProduto > 0 && quantidade > 0;
    }

    @Override
    public String toString() {
        return "Compra{" +
                "numeroNotaFiscal=" + numeroNotaFiscal +
                ", codigoFornecedor=" + codigoFornecedor +
                ", dataCompra=" + dataCompra +
                ", codigoProduto=" + codigoProduto +
                ", quantidade=" + quantidade +
                '}';
    }
}