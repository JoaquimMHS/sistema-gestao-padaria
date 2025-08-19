package org.padaria.model;

import java.time.LocalDate;

public abstract class Cliente implements IEntity {
    private int codigo;
    private String nome;
    private String endereco;
    private String telefone;
    private LocalDate dataCadastro;
    protected TipoCliente tipo;

    public Cliente() {
    }

    public Cliente(int codigo, String nome, String endereco, String telefone, LocalDate dataCadastro,
            TipoCliente tipo) {
        this.codigo = codigo;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.dataCadastro = dataCadastro;
        this.tipo = tipo;
    }

    @Override
    public int getCodigo() {
        return codigo;
    }

    @Override
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    @Override
    public boolean isValid() {
        return codigo > 0 &&
                nome != null && !nome.isEmpty() &&
                endereco != null && !endereco.isEmpty() &&
                telefone != null && !telefone.isEmpty() &&
                dataCadastro != null;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public LocalDate getDataCadastro() {
        return dataCadastro;
    }

    public void setDataCadastro(LocalDate dataCadastro) {
        this.dataCadastro = dataCadastro;
    }

    public TipoCliente getTipo() {
        return tipo;
    }

    public void setTipo(TipoCliente tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Cliente [codigo=" + codigo + ", nome=" + nome + ", endereco=" + endereco +
                ", telefone=" + telefone + ", dataCadastro=" + dataCadastro + ", tipo=" + tipo + "]";
    }
}