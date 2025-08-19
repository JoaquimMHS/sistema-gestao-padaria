package org.padaria.report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.padaria.model.Compra;
import org.padaria.model.Fornecedor;
import org.padaria.model.Produto;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;

public class RelatorioContasPagar implements IRelatorio {

    private final FornecedorService fornecedorService;
    private final CompraService compraService;
    private final ProdutoService produtoService;

    public RelatorioContasPagar(FornecedorService fornecedorService,
            CompraService compraService,
            ProdutoService produtoService) {
        this.fornecedorService = fornecedorService;
        this.compraService = compraService;
        this.produtoService = produtoService;
    }

    @Override
    public void gerar(String nomeArquivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(nomeArquivo))) {
            String[] cabecalho = getCabecalho();
            writer.println(String.join(";", cabecalho));

            List<String[]> dados = processarDados();
            for (String[] linha : dados) {
                writer.println(String.join(";", linha));
            }
        } catch (IOException e) {
            System.err.println("Erro ao gerar relatório de contas a pagar: " + e.getMessage());
            System.out.println("Erro de I/O.");
            System.exit(1);
        }
    }

    @Override
    public List<String[]> processarDados() {
        List<Compra> compras = compraService.listar();
        Map<Integer, Fornecedor> fornecedores = fornecedorService.listar().stream()
                .collect(Collectors.toMap(Fornecedor::getCodigo, f -> f));
        Map<Integer, Produto> produtos = produtoService.listar().stream()
                .collect(Collectors.toMap(Produto::getCodigo, p -> p));

        // Calcula o valor total por fornecedor
        Map<Integer, Double> totalPorFornecedor = new TreeMap<>();

        for (Compra compra : compras) {
            Produto produto = produtos.get(compra.getCodigoProduto());
            if (produto != null) {
                double valorCompra = produto.getValorCusto() * compra.getQuantidade();
                totalPorFornecedor.merge(compra.getCodigoFornecedor(), valorCompra, Double::sum);
            }
        }

        // Formata os dados para a saída CSV
        List<String[]> resultado = new ArrayList<>();

        totalPorFornecedor.forEach((codFornecedor, total) -> {
            Fornecedor fornecedor = fornecedores.get(codFornecedor);
            if (fornecedor != null) {
                resultado.add(new String[] {
                        fornecedor.getNome(),
                        fornecedor.getCNPJ(),
                        fornecedor.getPessoaContato(),
                        fornecedor.getTelefone(),
                        String.format("%.2f", total).replace(',', '.') // Garante ponto como separador decimal
                });
            }
        });

        // Ordena pelo nome do fornecedor (case-insensitive)
        resultado.sort((a, b) -> a[0].compareToIgnoreCase(b[0]));

        return resultado;
    }

    @Override
    public String[] getCabecalho() {
        return new String[] {
                "nome do fornecedor",
                "cnpj do fornecedor",
                "pessoa de contato",
                "telefone",
                "valor total a pagar"
        };
    }
}