package org.padaria.model;

import java.time.LocalDate;

public class PessoaFisica extends Cliente {
    private String cpf;

    public PessoaFisica() {
        super();
        this.tipo = TipoCliente.PESSOA_FISICA;
    }

    public PessoaFisica(int codigo, String nome, String endereco, String telefone,
            LocalDate dataCadastro, String cpf) {
        super(codigo, nome, endereco, telefone, dataCadastro, TipoCliente.PESSOA_FISICA);
        this.cpf = cpf;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    @Override
    public String toString() {
        return super.toString() + " PessoaFisica [cpf=" + cpf + "]";
    }
}