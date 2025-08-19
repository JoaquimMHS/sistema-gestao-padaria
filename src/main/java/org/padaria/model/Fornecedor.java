package org.padaria.model;

public class Fornecedor implements IEntity {
    private int codigo;
    private String nome;
    private String endereco;
    private String telefone;
    private String CNPJ;
    private String pessoaContato;

    public Fornecedor() {}

    public Fornecedor(int codigo, String nome, String endereco, String telefone, String CNPJ, String pessoaContato){
        this.codigo = codigo;
        this.nome = nome;
        this.endereco = endereco;
        this.telefone = telefone;
        this.CNPJ = CNPJ;
        this.pessoaContato = pessoaContato;
    }

    @Override
    public int getCodigo() {
        return codigo;
    }
    public String getNome() {
        return nome;
    }
    public String getCNPJ() {
        return CNPJ;
    }

    public String getEndereco() {
        return endereco;
    }

    public String getPessoaContato() {
        return pessoaContato;
    }

    public String getTelefone() {
        return telefone;
    }

    @Override
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    @Override
    public boolean isValid() {
        return this.codigo > 0 && this.nome != null && !this.nome.trim().isEmpty() && this.CNPJ != null && !this.CNPJ.trim().isEmpty();
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public void setCNPJ(String CNPJ) {
        this.CNPJ = CNPJ;
    }

    public void setPessoaContato(String pessoaContato) {
        this.pessoaContato = pessoaContato;
    }


    @Override
    public String toString() {
        return "Código: %d\n Nome: %s\n Endereço: %s\n Telefone: %s\n CNPJ: %s\n Pessoa Contato: %s\n";
    }
}
