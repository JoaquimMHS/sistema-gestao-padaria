package org.padaria.io;

import org.padaria.model.Fornecedor;
import org.padaria.util.CSVUtil; // Importa a nova classe utilitária
import org.padaria.util.IOExceptionHandler;

import java.util.ArrayList;
import java.util.List;


public class FornecedorIO implements ICSVReadable<Fornecedor> {

    @Override
    public List<Fornecedor> lerCSV(String caminhoArquivo) throws IOExceptionHandler {

        List<String[]> linhas = CSVUtil.lerArquivoCSV(caminhoArquivo);
        List<Fornecedor> fornecedores = new ArrayList<>();


        for (String[] campos : linhas) {
            Fornecedor fornecedor = parsearLinhaCSV(campos);
            if (fornecedor != null) {
                fornecedores.add(fornecedor);
            }
        }
        return fornecedores;
    }

    @Override
    public void salvarCSV(List<Fornecedor> lista, String caminhoArquivo) throws IOExceptionHandler {

        String[] cabecalho = {"código", "nome", "endereço", "telefone", "cnpj", "pessoa de contato"};
        List<String[]> dados = new ArrayList<>();


        for (Fornecedor fornecedor : lista) {
            dados.add(gerarLinhaCSV(fornecedor));
        }

        CSVUtil.escreverArquivoCSV(caminhoArquivo, dados, cabecalho);
    }

    @Override
    public String[] gerarLinhaCSV(Fornecedor entidade) {
        if (entidade == null) {
            return new String[0];
        }

        return new String[]{
                String.valueOf(entidade.getCodigo()),
                entidade.getNome() != null ? entidade.getNome() : "",
                entidade.getEndereco() != null ? entidade.getEndereco() : "",
                entidade.getTelefone() != null ? entidade.getTelefone() : "",
                entidade.getCNPJ() != null ? entidade.getCNPJ() : "",
                entidade.getPessoaContato() != null ? entidade.getPessoaContato() : ""
        };
    }

    @Override
    public Fornecedor parsearLinhaCSV(String[] campos) {
        if (campos == null || campos.length < 6) {
            return null;
        }

        try {
            int codigo = Integer.parseInt(campos[0].trim());
            String nome = campos[1].trim();
            String endereco = campos[2].trim();
            String telefone = campos[3].trim();
            String cnpj = campos[4].trim();
            String pessoaContato = campos[5].trim();

            return new Fornecedor(codigo, nome, endereco, telefone, cnpj, pessoaContato);
        } catch (NumberFormatException e) {
            return null;
        }
    }
}