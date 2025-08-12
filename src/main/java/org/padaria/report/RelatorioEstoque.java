package org.padaria.report;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.padaria.model.Produto;
import org.padaria.util.CSVUtil; 

public class RelatorioEstoque implements IRelatorio {

    private final List<Produto> produtos;

    public RelatorioEstoque(List<Produto> produtos) {
        this.produtos = produtos;
    }

    @Override
    public String[] getCabecalho() {
        return new String[]{"codigo_produto", "descricao_produto", "quantidade_estoque", "observacoes"};
    }

    @Override
    public List<String[]> processarDados() {
        this.produtos.sort(Comparator.comparing(Produto::getDescricao));
        List<String[]> dadosProcessados = new ArrayList<>();
        for (Produto p : this.produtos) {
            String observacao = p.getEstoqueAtual() < p.getEstoqueMinimo() ? "COMPRAR MAIS" : "";
            String[] linha = {
                String.valueOf(p.getCodigo()),
                p.getDescricao(),
                String.valueOf(p.getEstoqueAtual()),
                observacao
            };
            dadosProcessados.add(linha);
        }
        return dadosProcessados;
    }

    @Override
    public void gerar(String arquivoSaida) {
        String[] cabecalho = getCabecalho();
        List<String[]> dados = processarDados();

        CSVUtil.escreverArquivoCSV(arquivoSaida, dados, cabecalho);

        System.out.println("Relatório de estoque gerado com sucesso em: " + arquivoSaida);
    }
}