package org.padaria.report;

import org.padaria.model.Venda;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;
import org.padaria.util.IOExceptionHandler;

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
        System.out.println("Relatório gerado em: " + nomeArquivo);
    }

    @Override
    public List<String[]> processarDados() {
        List<Venda> vendas = vendaService.listar();
        List<RelatorioItemProduto> itens = new ArrayList<>();

        for (Venda venda : vendas) {
            int codigoProduto = venda.getCodigoProduto();
            String descricao = produtoService.getDescricao(codigoProduto);
            double precoVenda = produtoService.getPrecoVenda(codigoProduto);
            double precoCusto = produtoService.getPrecoCusto(codigoProduto);

            // Busca o produto
            RelatorioItemProduto itemExistente = null;
            for (RelatorioItemProduto item : itens) {
                if (item.getCodigoProduto() == codigoProduto) {
                    itemExistente = item;
                    break;
                }
            }

            // cria novo item se não existir
            if (itemExistente == null) {
                itemExistente = new RelatorioItemProduto(codigoProduto, descricao);
                itens.add(itemExistente);
            }

            itemExistente.adicionarVenda(precoVenda, precoCusto, venda.getQuantidade());
        }

        // ordenação
        itens.sort((a, b) -> {
            int comparacaoLucro = Double.compare(b.getLucro(), a.getLucro());
            return comparacaoLucro != 0 ? comparacaoLucro : Integer.compare(a.getCodigoProduto(), b.getCodigoProduto());
        });

        // coloca no csv como array de strings
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