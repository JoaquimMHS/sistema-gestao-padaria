package org.padaria.io;

import java.util.List;
import java.util.stream.Collectors;

import org.padaria.model.Produto;
import org.padaria.util.CSVUtil;

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
        List<String[]> linhas = CSVUtil.lerArquivoCSV(arquivo);

        return linhas.stream()
                .map(this::parsearLinhaCSV)
                .collect(Collectors.toList());
    }

    @Override
    public void salvarCSV(List<Produto> produtos, String arquivo) {
        String[] cabecalho = {"codigo", "descricao", "estoque_minimo", "estoque_atual", "valor_custo", "percentual_lucro"};

        List<String[]> dados = produtos.stream()
                .map(this::gerarLinhaCSV)
                .collect(Collectors.toList());
        
        CSVUtil.escreverArquivoCSV(arquivo, dados, cabecalho);
    }
}