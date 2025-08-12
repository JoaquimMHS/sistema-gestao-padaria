package org.padaria.report;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.padaria.model.Produto; 

public class RelatorioEstoque implements IRelatorio {

    private List<Produto> produtos;

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

        try (BufferedWriter bw = Files.newBufferedWriter(Paths.get(arquivoSaida))) {
            bw.write(String.join(";", cabecalho));
            bw.newLine();

            for (String[] linha : dados) {
                bw.write(String.join(";", linha));
                bw.newLine();
            }

            System.out.println("Relatório de estoque gerado com sucesso em: " + arquivoSaida);

        } catch (IOException e) {
            System.out.println("Erro de I/O.");
            System.exit(1);
        }
    }
}