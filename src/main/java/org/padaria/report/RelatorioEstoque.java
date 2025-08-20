package org.padaria.report;

import org.padaria.service.ProdutoService;
import org.padaria.util.IOExceptionHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class RelatorioEstoque implements IRelatorio {
    private final ProdutoService produtoService;

    public RelatorioEstoque(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Override
    public void gerar(String nomeArquivo) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            String[] cabecalho = getCabecalho();
            writer.println(String.join(";", cabecalho));

            List<String[]> dados = processarDados();
            for (String[] linha : dados) {
                writer.println(String.join(";", linha));
            }
        } catch (IOException e) {
            IOExceptionHandler.handle("Erro ao gerar o relatório", e);
        }
        System.out.println("Relatório de estoque gerado em: " + nomeArquivo);
    }

    @Override
    public List<String[]> processarDados() {
        return produtoService.listar().stream()
                .sorted(Comparator.comparing(p -> p.getDescricao().toLowerCase()))
                .map(p -> {
                    String observacao = p.getEstoqueAtual() < p.getEstoqueMinimo() ? "COMPRAR MAIS" : "";
                    return new String[] {
                            String.valueOf(p.getCodigo()),
                            p.getDescricao(),
                            String.valueOf(p.getEstoqueAtual()),
                            observacao
                    };
                })
                .collect(Collectors.toList());
    }

    @Override
    public String[] getCabecalho() {
        return new String[] {
                "código do produto",
                "descrição do produto",
                "quantidade em estoque",
                "observações"
        };
    }
}