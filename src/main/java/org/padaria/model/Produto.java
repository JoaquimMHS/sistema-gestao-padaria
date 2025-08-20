package org.padaria.model;

public class Produto implements IEntity {

    private int codigo;
    private String descricao;
    private int estoqueMinimo;
    private int estoqueAtual;
    private double valorCusto;
    private int percentualLucro;

    public Produto() {
    }

    public Produto(int codigo, String descricao, int estoqueMinimo, int estoqueAtual, double valorCusto,
            int percentualLucro) {
        this.codigo = codigo;
        this.descricao = descricao;
        this.estoqueMinimo = estoqueMinimo;
        this.estoqueAtual = estoqueAtual;
        this.valorCusto = valorCusto;
        this.percentualLucro = percentualLucro;
    }

    @Override
    public boolean isValid() {
        return this.codigo > 0 && this.descricao != null && !this.descricao.trim().isEmpty();
    }

    @Override
    public int getCodigo() {
        return codigo;
    }

    @Override
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public int getEstoqueMinimo() {
        return estoqueMinimo;
    }

    public void setEstoqueMinimo(int estoqueMinimo) {
        this.estoqueMinimo = estoqueMinimo;
    }

    public int getEstoqueAtual() {
        return estoqueAtual;
    }

    public void setEstoqueAtual(int estoqueAtual) {
        this.estoqueAtual = estoqueAtual;
    }

    public double getValorCusto() {
        return valorCusto;
    }

    public void setValorCusto(double valorCusto) {
        this.valorCusto = valorCusto;
    }

    public int getPercentualLucro() {
        return percentualLucro;
    }

    public void setPercentualLucro(int percentualLucro) {
        this.percentualLucro = percentualLucro;
    }

    public double calcularValorVenda() {
        return this.valorCusto + (this.valorCusto * (this.percentualLucro / 100.0));
    }

    @Override
    public String toString() {
        return "Produto: " +
                "código=" + codigo +
                ", descrição='" + descricao + '\'' +
                ", estoqueAtual=" + estoqueAtual +
                ", valorVenda=" + String.format("R$%.2f", calcularValorVenda());
    }
}
