// src/main/java/org/padaria/report/RelatorioVendasProduto.java
package org.padaria.report;

import org.padaria.model.Venda;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class RelatorioVendasProduto implements IRelatorio {
    private final VendaService vendaService;
    private final ProdutoService produtoService;

    // Classe interna estática para representar um item do relatório
    private static class RelatorioItemProduto {
        private final int codigoProduto;
        private final String descricaoProduto;
        private double receitaBruta;
        private double lucro;

        public RelatorioItemProduto(int codigoProduto, String descricaoProduto) {
            this.codigoProduto = codigoProduto;
            this.descricaoProduto = descricaoProduto;
        }

        public void adicionarVenda(double valorVenda, double valorCusto, int quantidade) {
            this.receitaBruta += valorVenda * quantidade;
            this.lucro += (valorVenda - valorCusto) * quantidade;
        }

        // Getters
        public int getCodigoProduto() { return codigoProduto; }
        public String getDescricaoProduto() { return descricaoProduto; }
        public double getReceitaBruta() { return receitaBruta; }
        public double getLucro() { return lucro; }
    }

    public RelatorioVendasProduto(VendaService vendaService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.produtoService = produtoService;
    }

    @Override
    public void gerar(String nomeArquivo) throws IOException {
        System.out.println("Gerando relatório de vendas por produto...");

        // Processa e obtém os dados já ordenados
        List<String[]> dadosOrdenados = processarDados();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo))) {
            bw.write(getCabecalho());
            bw.newLine();
            for (String[] linha : dadosOrdenados) {
                bw.write(String.join(";", linha));
                bw.newLine();
            }
        }
        System.out.println("Relatório gerado com sucesso em: " + nomeArquivo);
    }

    @Override
    public List<String[]> processarDados() {
        Map<Integer, RelatorioItemProduto> relatorioMap = vendaService.listar().stream()
                .collect(Collectors.toMap(
                        Venda::getCodigoProduto,
                        venda -> {
                            // Assumindo que você tem um método para obter a descrição do produto
                            String descricao = produtoService.getDescricao(venda.getCodigoProduto());
                            RelatorioItemProduto item = new RelatorioItemProduto(venda.getCodigoProduto(), descricao);
                            item.adicionarVenda(
                                    produtoService.getPrecoVenda(venda.getCodigoProduto()),
                                    produtoService.getPrecoCusto(venda.getCodigoProduto()),
                                    venda.getQuantidade()
                            );
                            return item;
                        },
                        (itemExistente, novoItem) -> {
                            itemExistente.receitaBruta += novoItem.receitaBruta;
                            itemExistente.lucro += novoItem.lucro;
                            return itemExistente;
                        }
                ));

        // Converte o Map para uma lista de arrays de string e aplica a ordenação
        List<String[]> dadosOrdenados = relatorioMap.values().stream()
                .sorted(Comparator.comparing(RelatorioItemProduto::getLucro).reversed()
                        .thenComparing(RelatorioItemProduto::getCodigoProduto))
                .map(item -> new String[]{
                        String.valueOf(item.getCodigoProduto()),
                        item.getDescricaoProduto(),
                        String.format("%.2f", item.getReceitaBruta()),
                        String.format("%.2f", item.getLucro())
                })
                .collect(Collectors.toList());

        return dadosOrdenados;
    }
    @Override
    public String getCabecalho() {
        return "codigo do produto;descricao do produto;receita bruta;lucro";
    }
}