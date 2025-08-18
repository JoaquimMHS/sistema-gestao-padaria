package org.padaria.io;

import org.padaria.model.Compra;
import org.padaria.util.IOExceptionHandler;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CompraIO implements ICSVReadable<Compra> {

    @Override
    public List<Compra> lerCSV(String caminhoArquivo) throws IOExceptionHandler {
        List<Compra> compras = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            br.readLine(); // Pula o cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                compras.add(parsearLinhaCSV(linha.split(";")));
            }
        } catch (IOException e) {
            IOExceptionHandler.handle("Erro ao ler o arquivo CSV de compras.", e);
        }
        return compras;
    }

    @Override
    public void salvarCSV(List<Compra> lista, String caminhoArquivo) throws IOExceptionHandler {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            bw.write("numero_nota_fiscal;codigo_fornecedor;data_compra;codigo_produto;quantidade");
            bw.newLine();
            for (Compra compra : lista) {
                String[] campos = gerarLinhaCSV(compra);
                String linha = String.join(";", campos);
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            IOExceptionHandler.handle("Erro ao salvar o arquivo CSV de compras.", e);
        }
    }

    @Override
    public String[] gerarLinhaCSV(Compra entidade) {
        return new String[]{
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