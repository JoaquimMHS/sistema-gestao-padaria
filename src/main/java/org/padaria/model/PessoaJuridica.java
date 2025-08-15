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
        return super.isValid() && cnpj != null && !cnpj.isEmpty() && 
               validarCNPJ(cnpj) && inscricaoEstadual > 0;
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

    private boolean validarCNPJ(String cnpj) {
        // Implementação da validação de CNPJ
        cnpj = cnpj.replaceAll("[^0-9]", "");
        
        if (cnpj.length() != 14) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cnpj.matches("(\\d)\\1{13}")) {
            return false;
        }
        
        // Cálculo do primeiro dígito verificador
        int soma = 0;
        int peso = 5;
        for (int i = 0; i < 12; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
            peso = (peso == 2) ? 9 : peso - 1;
        }
        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;
        
        // Cálculo do segundo dígito verificador
        soma = 0;
        peso = 6;
        for (int i = 0; i < 13; i++) {
            soma += Character.getNumericValue(cnpj.charAt(i)) * peso;
            peso = (peso == 2) ? 9 : peso - 1;
        }
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;
        
        // Verifica se os dígitos calculados conferem com os informados
        return Character.getNumericValue(cnpj.charAt(12)) == digito1 && 
               Character.getNumericValue(cnpj.charAt(13)) == digito2;
    }

    @Override
    public String toString() {
        return super.toString() + " PessoaJuridica [cnpj=" + cnpj + 
               ", inscricaoEstadual=" + inscricaoEstadual + "]";
    }
}