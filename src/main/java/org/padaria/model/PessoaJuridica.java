package org.padaria.model;

import java.time.LocalDate;

public class PessoaJuridica extends Cliente {
    private String cnpj;
    private int inscricaoEstadual;

    public PessoaJuridica() {
        super();
        this.tipo = TipoCliente.PESSOA_JURIDICA;
    }

    public PessoaJuridica(int codigo, String nome, String endereco, String telefone,
            LocalDate dataCadastro, String cnpj, int inscricaoEstadual) {
        super(codigo, nome, endereco, telefone, dataCadastro, TipoCliente.PESSOA_JURIDICA);
        this.cnpj = cnpj;
        this.inscricaoEstadual = inscricaoEstadual;
    }

    @Override
    public boolean isValid() {
        return super.isValid() && cnpj != null && !cnpj.isEmpty() && inscricaoEstadual > 0;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public int getInscricaoEstadual() {
        return inscricaoEstadual;
    }

    public void setInscricaoEstadual(int inscricaoEstadual) {
        this.inscricaoEstadual = inscricaoEstadual;
    }

    @Override
    public String toString() {
        return super.toString() + " PessoaJuridica [cnpj=" + cnpj +
                ", Inscrição Estadual=" + inscricaoEstadual + "]";
    }
}