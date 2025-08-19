package org.padaria.io;

import org.padaria.model.Produto;
import org.padaria.util.CSVUtil;
import org.padaria.util.IOExceptionHandler;

import java.util.ArrayList;
import java.util.List;

public class ProdutoIO implements ICSVReadable<Produto> {

    @Override
    public List<Produto> lerCSV(String caminhoArquivo) throws IOExceptionHandler {
        List<Produto> produtos = new ArrayList<>();
        List<String[]> linhas = CSVUtil.lerArquivoCSV(caminhoArquivo);
        for (String[] campos : linhas) {
            produtos.add(parsearLinhaCSV(campos));
        }
        return produtos;
    }

    @Override
    public void salvarCSV(List<Produto> lista, String caminhoArquivo) throws IOExceptionHandler {
        String[] cabecalho = { "codigo", "descricao", "estoque_minimo", "estoque_atual", "valor_custo",
                "percentual_lucro" };
        List<String[]> dados = new ArrayList<>();

        for (Produto produto : lista) {
            dados.add(gerarLinhaCSV(produto));
        }
        CSVUtil.escreverArquivoCSV(caminhoArquivo, dados, cabecalho);
    }

    @Override
    public String[] gerarLinhaCSV(Produto entidade) {
        return new String[] {
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