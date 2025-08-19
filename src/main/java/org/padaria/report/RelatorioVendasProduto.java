package org.padaria.report;

import org.padaria.model.Venda;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RelatorioVendasProduto implements IRelatorio {
    private final VendaService vendaService;
    private final ProdutoService produtoService;

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

        public int getCodigoProduto() {
            return codigoProduto;
        }

        public String getDescricaoProduto() {
            return descricaoProduto;
        }

        public double getReceitaBruta() {
            return receitaBruta;
        }

        public double getLucro() {
            return lucro;
        }
    }

    public RelatorioVendasProduto(VendaService vendaService, ProdutoService produtoService) {
        this.vendaService = vendaService;
        this.produtoService = produtoService;
    }

    @Override
    public void gerar(String nomeArquivo) throws IOException {
        System.out.println("Gerando relatório de vendas por produto...");

        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            String[] cabecalho = getCabecalho();
            writer.println(String.join(";", cabecalho));

            List<String[]> dados = processarDados();
            for (String[] linha : dados) {
                writer.println(String.join(";", linha));
            }
        } catch (IOException e) {
            System.err.println("Erro ao gerar relatório: " + e.getMessage());
            System.exit(1);
        }
        System.out.println("Relatório gerado com sucesso em: " + nomeArquivo);
    }

    @Override
    public List<String[]> processarDados() {
        List<Venda> vendas = vendaService.listar();
        List<RelatorioItemProduto> itens = new ArrayList<>();

        // Processa cada venda
        for (Venda venda : vendas) {
            int codigoProduto = venda.getCodigoProduto();
            String descricao = produtoService.getDescricao(codigoProduto);
            double precoVenda = produtoService.getPrecoVenda(codigoProduto);
            double precoCusto = produtoService.getPrecoCusto(codigoProduto);

            // Busca se já existe um item para este produto
            RelatorioItemProduto itemExistente = null;
            for (RelatorioItemProduto item : itens) {
                if (item.getCodigoProduto() == codigoProduto) {
                    itemExistente = item;
                    break;
                }
            }

            // Se não existe, cria novo item
            if (itemExistente == null) {
                itemExistente = new RelatorioItemProduto(codigoProduto, descricao);
                itens.add(itemExistente);
            }

            // Adiciona os valores da venda
            itemExistente.adicionarVenda(precoVenda, precoCusto, venda.getQuantidade());
        }

        // Ordena por lucro (decrescente) e depois por código do produto
        itens.sort((a, b) -> {
            int comparacaoLucro = Double.compare(b.getLucro(), a.getLucro());
            return comparacaoLucro != 0 ? comparacaoLucro : Integer.compare(a.getCodigoProduto(), b.getCodigoProduto());
        });

        // Converte para array de strings
        List<String[]> resultado = new ArrayList<>();
        for (RelatorioItemProduto item : itens) {
            resultado.add(new String[] {
                    String.valueOf(item.getCodigoProduto()),
                    item.getDescricaoProduto(),
                    String.format("%.2f", item.getReceitaBruta()),
                    String.format("%.2f", item.getLucro())
            });
        }

        return resultado;
    }

    @Override
    public String[] getCabecalho() {
        return new String[] {
                "codigo do produto",
                "descrição do produto",
                "receita bruta",
                "lucro"
        };
    }
}