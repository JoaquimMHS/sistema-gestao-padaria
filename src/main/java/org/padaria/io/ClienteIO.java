package org.padaria.io;

import org.padaria.model.Cliente;
import org.padaria.model.PessoaFisica;
import org.padaria.model.PessoaJuridica;
import org.padaria.model.TipoCliente;
import org.padaria.util.CSVUtil;
import org.padaria.util.IOExceptionHandler;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class ClienteIO implements ICSVReadable<Cliente> {

    // usado pra colocar a data no padrão br
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    @Override
    public List<Cliente> lerCSV(String caminhoArquivo) throws IOExceptionHandler {
        List<Cliente> clientes = new ArrayList<>();
        List<String[]> linhas = CSVUtil.lerArquivoCSV(caminhoArquivo);
        for (String[] campos : linhas) {
            clientes.add(parsearLinhaCSV(campos));
        }
        return clientes;
    }

    @Override
    public void salvarCSV(List<Cliente> lista, String caminhoArquivo) throws IOExceptionHandler {
        List<String[]> dados = new ArrayList<>();
        String[] cabecalho = new String[] { "código", "nome", "endereço", "telefone", "data de cadastro",
                "tipo de cliente", "cpf ou cnpj", "número de inscrição estadual" };
        for (Cliente cliente : lista) {
            dados.add(gerarLinhaCSV(cliente));
        }
        CSVUtil.escreverArquivoCSV(caminhoArquivo, dados, cabecalho);
    }

    @Override
    public String[] gerarLinhaCSV(Cliente entidade) {
        String[] linha = new String[8];
        linha[0] = String.valueOf(entidade.getCodigo());
        linha[1] = entidade.getNome();
        linha[2] = entidade.getEndereco();
        linha[3] = entidade.getTelefone();
        linha[4] = entidade.getDataCadastro().format(DATE_FORMATTER);
        linha[5] = entidade.getTipo().getCodigo();

        if (entidade instanceof PessoaFisica) {
            PessoaFisica pf = (PessoaFisica) entidade;
            linha[6] = pf.getCpf();
            linha[7] = ""; // Inscrição estadual vazia para PF
        } else if (entidade instanceof PessoaJuridica) {
            PessoaJuridica pj = (PessoaJuridica) entidade;
            linha[6] = pj.getCnpj();
            linha[7] = String.valueOf(pj.getInscricaoEstadual());
        }

        return linha;
    }

    @Override
    public Cliente parsearLinhaCSV(String[] campos) {
        int codigo = Integer.parseInt(campos[0]);
        String nome = campos[1];
        String endereco = campos[2];
        String telefone = campos[3];
        LocalDate dataCadastro = LocalDate.parse(campos[4], DATE_FORMATTER);
        TipoCliente tipo = TipoCliente.fromCodigo(campos[5]);

        if (tipo == TipoCliente.PESSOA_FISICA) {
            String cpf = campos[6];
            return new PessoaFisica(codigo, nome, endereco, telefone, dataCadastro, cpf);
        } else if (tipo == TipoCliente.PESSOA_JURIDICA) {
            String cnpj = campos[6];
            int inscricaoEstadual = Integer.parseInt(campos[7]);
            return new PessoaJuridica(codigo, nome, endereco, telefone, dataCadastro, cnpj, inscricaoEstadual);
        }

        return null; // ou lançar uma exceção para tipo de cliente inválido
    }
}