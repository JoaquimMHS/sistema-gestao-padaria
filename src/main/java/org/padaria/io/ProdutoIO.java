package org.padaria.io; 

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.padaria.model.Produto;

public class ProdutoIO implements ICSVReadable<Produto> {

    @Override
    public Produto parsearLinhaCSV(String[] campos) {
        int codigo = Integer.parseInt(campos[0]);
        String descricao = campos[1];
        int estoqueMinimo = Integer.parseInt(campos[2]);
        int estoqueAtual = Integer.parseInt(campos[3]);
        double valorCusto = Double.parseDouble(campos[4].replace(",", "."));
        int percentualLucro = Integer.parseInt(campos[5]);

        return new Produto(codigo, descricao, estoqueMinimo, estoqueAtual, valorCusto, percentualLucro);
    }

    @Override
    public String[] gerarLinhaCSV(Produto produto) {
        return new String[]{
            String.valueOf(produto.getCodigo()),
            produto.getDescricao(),
            String.valueOf(produto.getEstoqueMinimo()),
            String.valueOf(produto.getEstoqueAtual()),
            String.valueOf(produto.getValorCusto()),
            String.valueOf(produto.getPercentualLucro())
        };
    }

    @Override
    public List<Produto> lerCSV(String arquivo) {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(arquivo))) {
            br.readLine();

            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty()) continue;
                
                String[] campos = linha.split(";");
                Produto produto = parsearLinhaCSV(campos); 
                produtos.add(produto);
            }
        } catch (IOException e) {
            System.out.println("Erro de I/O.");
            System.exit(1);
        }
        return produtos;
    }

    @Override
    public void salvarCSV(List<Produto> produtos, String arquivo) {
        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(arquivo))) {
            String cabecalho = "codigo;descricao;estoque_minimo;estoque_atual;valor_custo;percentual_lucro";
            bw.write(cabecalho);
            bw.newLine();

            for (Produto p : produtos) {
                String[] campos = gerarLinhaCSV(p);
                String linhaCSV = String.join(";", campos);
                bw.write(linhaCSV);
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro de I/O.");
            System.exit(1);
        }
    }
}