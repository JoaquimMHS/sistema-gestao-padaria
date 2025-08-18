// src/main/java/org/padaria/report/RelatorioEstoque.java
package org.padaria.report;

import org.padaria.model.Produto;
import org.padaria.service.ProdutoService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioEstoque implements IRelatorio {
    private final ProdutoService produtoService;

    public RelatorioEstoque(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Override
    public void gerar(String nomeArquivo) throws IOException {
        System.out.println("Gerando relatório de estoque...");
        List<String[]> dados = processarDados();
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo))) {
            bw.write(getCabecalho());
            bw.newLine();
            for (String[] linha : dados) {
                bw.write(String.join(";", linha));
                bw.newLine();
            }
        }
        System.out.println("Relatório de estoque gerado em: " + nomeArquivo);
    }

    @Override
    public List<String[]> processarDados() {
        return produtoService.listar().stream()
                .map(p -> {
                    String observacao = p.getEstoqueAtual() < p.getEstoqueMinimo() ? "COMPRAR MAIS" : "";
                    return new String[]{
                            String.valueOf(p.getCodigo()),
                            p.getDescricao(),
                            String.valueOf(p.getEstoqueAtual()),
                            observacao
                    };
                })
                .collect(Collectors.toList());
    }

    @Override
    public String getCabecalho() {
        return "codigo do produto;descricao do produto;quantidade em estoque;observacoes";
    }
}