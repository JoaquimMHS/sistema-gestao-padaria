package org.padaria.report;

import org.padaria.model.ModoPagamento;
import org.padaria.model.Venda;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;
import org.padaria.util.IOExceptionHandler;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RelatorioVendasPagamento implements IRelatorio {
    private final VendaService vendaService;
    private final ProdutoService produtoService;

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

        public ModoPagamento getModoPagamento() {
            return modoPagamento;
        }

        public double getReceitaBruta() {
            return receitaBruta;
        }

        public double getLucro() {
            return lucro;
        }
    }

    public RelatorioVendasPagamento(VendaService vendaService, ProdutoService produtoService) {
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
        System.out.println("Relatório gerado com sucesso em: " + nomeArquivo);
    }

    @Override
    public List<String[]> processarDados() {
        List<Venda> vendas = vendaService.listar();
        List<RelatorioItemPagamento> itens = new ArrayList<>();

        // caminha pela lista de vendas
        for (Venda venda : vendas) {
            ModoPagamento modoPagamento = venda.getModoPagamento();
            int codigoProduto = venda.getCodigoProduto();
            double precoVenda = produtoService.getPrecoVenda(codigoProduto);
            double precoCusto = produtoService.getPrecoCusto(codigoProduto);

            // busca se já existe um item para este modo de pagamento
            RelatorioItemPagamento itemExistente = null;
            for (RelatorioItemPagamento item : itens) {
                if (item.getModoPagamento() == modoPagamento) {
                    itemExistente = item;
                    break;
                }
            }

            if (itemExistente == null) {
                itemExistente = new RelatorioItemPagamento(modoPagamento);
                itens.add(itemExistente);
            }

            itemExistente.adicionarVenda(precoVenda, precoCusto, venda.getQuantidade());
        }

        // ordenado por lucro e depois por caractere do modo de pagamento
        itens.sort((a, b) -> {
            int comparacaoLucro = Double.compare(b.getLucro(), a.getLucro());
            if (comparacaoLucro != 0)
                return comparacaoLucro;
            return Character.compare(
                    a.getModoPagamento().getCaracter(),
                    b.getModoPagamento().getCaracter());
        });

        // coloca no csv como array de strings
        List<String[]> resultado = new ArrayList<>();
        for (RelatorioItemPagamento item : itens) {
            resultado.add(new String[] {
                    String.valueOf(item.getModoPagamento().getCaracter()),
                    String.format("%.2f", item.getReceitaBruta()),
                    String.format("%.2f", item.getLucro())
            });
        }

        return resultado;
    }

    @Override
    public String[] getCabecalho() {
        return new String[] {
                "modo de pagamento",
                "receita bruta",
                "lucro"
        };
    }
}