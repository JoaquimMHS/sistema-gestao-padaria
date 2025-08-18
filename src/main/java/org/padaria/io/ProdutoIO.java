// src/main/java/org/padaria/io/ProdutoIO.java
package org.padaria.io;

import org.padaria.model.Produto;
import org.padaria.util.IOExceptionHandler;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProdutoIO implements ICSVReadable<Produto> {

    @Override
    public List<Produto> lerCSV(String caminhoArquivo) throws IOExceptionHandler {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            br.readLine(); // Pula o cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                produtos.add(parsearLinhaCSV(linha.split(";")));
            }
        } catch (IOException e) {
            IOExceptionHandler.handle("Erro ao ler o arquivo CSV de produtos.", e);
        }
        return produtos;
    }

    @Override
    public void salvarCSV(List<Produto> lista, String caminhoArquivo) throws IOExceptionHandler {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            bw.write("codigo;descricao;estoque_minimo;estoque_atual;valor_custo;percentual_lucro");
            bw.newLine();
            for (Produto produto : lista) {
                String[] campos = gerarLinhaCSV(produto);
                String linha = String.join(";", campos);
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            IOExceptionHandler.handle("Erro ao salvar o arquivo CSV de produtos.", e);
        }
    }

    @Override
    public String[] gerarLinhaCSV(Produto entidade) {
        return new String[]{
                String.valueOf(entidade.getCodigo()),
                entidade.getDescricao(),
                String.valueOf(entidade.getEstoqueMinimo()),
                String.valueOf(entidade.getEstoqueAtual()),
                String.valueOf(entidade.getValorCusto()),
                String.valueOf(entidade.getPercentualLucro())
        };
    }

    @Override
    public Produto parsearLinhaCSV(String[] campos) {
        int codigo = Integer.parseInt(campos[0]);
        String descricao = campos[1];
        int estoqueMinimo = Integer.parseInt(campos[2]);
        int estoqueAtual = Integer.parseInt(campos[3]);
        double valorCusto = Double.parseDouble(campos[4]);
        int percentualLucro = Integer.parseInt(campos[5]);
        return new Produto(codigo, descricao, estoqueMinimo, estoqueAtual, valorCusto, percentualLucro);
    }
}