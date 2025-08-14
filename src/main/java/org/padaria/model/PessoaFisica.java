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

    @Override
    public boolean isValid() {
        return super.isValid() && cpf != null && !cpf.isEmpty() && validarCPF(cpf);
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    private boolean validarCPF(String cpf) {
        // Implementação da validação de CPF
        cpf = cpf.replaceAll("[^0-9]", "");
        
        if (cpf.length() != 11) {
            return false;
        }
        
        // Verifica se todos os dígitos são iguais
        if (cpf.matches("(\\d)\\1{10}")) {
            return false;
        }
        
        // Cálculo do primeiro dígito verificador
        int soma = 0;
        for (int i = 0; i < 9; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (10 - i);
        }
        int resto = soma % 11;
        int digito1 = (resto < 2) ? 0 : 11 - resto;
        
        // Cálculo do segundo dígito verificador
        soma = 0;
        for (int i = 0; i < 10; i++) {
            soma += Character.getNumericValue(cpf.charAt(i)) * (11 - i);
        }
        resto = soma % 11;
        int digito2 = (resto < 2) ? 0 : 11 - resto;
        
        // Verifica se os dígitos calculados conferem com os informados
        return Character.getNumericValue(cpf.charAt(9)) == digito1 && 
               Character.getNumericValue(cpf.charAt(10)) == digito2;
    }

    @Override
    public String toString() {
        return super.toString() + " PessoaFisica [cpf=" + cpf + "]";
    }
}