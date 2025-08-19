package org.padaria.io;

import org.padaria.model.Compra;
import org.padaria.util.CSVUtil;
import org.padaria.util.IOExceptionHandler;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraIO implements ICSVReadable<Compra> {

    @Override
    public List<Compra> lerCSV(String caminhoArquivo) throws IOExceptionHandler {
        List<Compra> compras = new ArrayList<>();
        List<String[]> linhas = CSVUtil.lerArquivoCSV(caminhoArquivo);
        for (String[] campos : linhas) {
            Compra compra = parsearLinhaCSV(campos);
            if (compra != null) {
                compras.add(compra);
            }
        }
        return compras;
    }

    @Override
    public void salvarCSV(List<Compra> lista, String caminhoArquivo) throws IOExceptionHandler {
        String[] cabecalho = { "numero_nota_fiscal", "codigo_fornecedor", "data_compra", "codigo_produto",
                "quantidade" };
        List<String[]> dados = new ArrayList<>();
        for (Compra compra : lista) {
            dados.add(gerarLinhaCSV(compra));
        }
        CSVUtil.escreverArquivoCSV(caminhoArquivo, dados, cabecalho);
    }

    @Override
    public String[] gerarLinhaCSV(Compra entidade) {
        return new String[] {
                String.valueOf(entidade.getNumeroNotaFiscal()),
                String.valueOf(entidade.getCodigoFornecedor()),
                entidade.getDataCompra().toString(),
                String.valueOf(entidade.getCodigoProduto()),
                String.valueOf(entidade.getQuantidade())
        };
    }

    @Override
    public Compra parsearLinhaCSV(String[] campos) {
        int numeroNotaFiscal = Integer.parseInt(campos[0]);
        int codigoFornecedor = Integer.parseInt(campos[1]);
        LocalDate dataCompra = LocalDate.parse(campos[2]);
        int codigoProduto = Integer.parseInt(campos[3]);
        int quantidade = Integer.parseInt(campos[4]);
        return new Compra(numeroNotaFiscal, codigoFornecedor, dataCompra, codigoProduto, quantidade);
    }
}