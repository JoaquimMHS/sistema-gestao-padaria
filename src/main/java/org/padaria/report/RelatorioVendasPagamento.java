// src/main/java/org/padaria/report/RelatorioVendasPagamento.java
package org.padaria.report;

import org.padaria.model.ModoPagamento;
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

public class RelatorioVendasPagamento implements IRelatorio {
    private final VendaService vendaService;
    private final ProdutoService produtoService;

    // Classe interna estática para representar um item do relatório
    private static class RelatorioItemPagamento {
        private ModoPagamento modoPagamento;
        private double receitaBruta;
        private double lucro;

        public RelatorioItemPagamento(ModoPagamento modoPagamento) {
            this.modoPagamento = modoPagamento;
        }

        public void adicionarVenda(double valorVenda, double valorCusto, int quantidade) {
            this.receitaBruta += valorVenda * quantidade;
            this.lucro += (valorVenda - valorCusto) * quantidade;
        }

        // Getters
        public ModoPagamento getModoPagamento() { return modoPagamento; }
        public double getReceitaBruta() { return receitaBruta; }
        public double getLucro() { return lucro; }
    }

    public RelatorioVendasPagamento(VendaService vendaService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.produtoService = produtoService;
    }

    // O método principal que orquestra a geração do relatório
    @Override
    public void gerar(String nomeArquivo) throws IOException {
        System.out.println("Gerando relatório de vendas por forma de pagamento...");

        // Processa os dados, obtendo uma lista de arrays de strings já ordenados
        List<String[]> dadosOrdenados = processarDados();

        // Salva os dados processados no arquivo CSV
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nomeArquivo))) {
            bw.write(getCabecalho());
            bw.newLine();
            for (String[] item : dadosOrdenados) {
                String linha = String.join(";", item);
                bw.write(linha);
                bw.newLine();
            }
        }
        System.out.println("Relatório gerado com sucesso em: " + nomeArquivo);
    }

    @Override
    public List<String[]> processarDados() {
        Map<ModoPagamento, RelatorioItemPagamento> relatorioMap = vendaService.listar().stream()
                .collect(Collectors.toMap(
                        Venda::getModoPagamento,
                        venda -> {
                            RelatorioItemPagamento item = new RelatorioItemPagamento(venda.getModoPagamento());
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

        // Converte o Map para List<String[]> e ordena os dados
        return relatorioMap.values().stream()
                .sorted(Comparator.comparing(RelatorioItemPagamento::getLucro).reversed()
                        .thenComparing(item -> item.getModoPagamento().getCaracter()))
                .map(item -> new String[]{
                        String.valueOf(item.getModoPagamento().getCaracter()),
                        String.format("%.2f", item.getReceitaBruta()),
                        String.format("%.2f", item.getLucro())
                })
                .collect(Collectors.toList());
    }

    @Override
    public String getCabecalho() {
        return "modo_pagamento;receita_bruta;lucro";
    }
}