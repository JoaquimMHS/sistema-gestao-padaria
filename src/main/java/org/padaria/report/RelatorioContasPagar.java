package org.padaria.report;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.padaria.model.Compra;
import org.padaria.model.Fornecedor;
import org.padaria.model.Produto;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;
import org.padaria.util.IOExceptionHandler;

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
            IOExceptionHandler.handle("Erro ao gerar o relatório", e);
        }
    }

    @Override
    public List<String[]> processarDados() {
        List<Compra> compras = compraService.listar();
        List<Fornecedor> fornecedores = fornecedorService.listar();
        List<Produto> produtos = produtoService.listar();

        List<String[]> resultado = new ArrayList<>();

        // Para cada fornecedor, calcula o total a pagar
        for (Fornecedor fornecedor : fornecedores) {
            double totalPagar = 0.0;

            for (Compra compra : compras) {
                if (compra.getCodigoFornecedor() == fornecedor.getCodigo()) {
                    for (Produto produto : produtos) {
                        if (produto.getCodigo() == compra.getCodigoProduto()) {
                            totalPagar += produto.getValorCusto() * compra.getQuantidade();
                            break;
                        }
                    }
                }
            }

            if (totalPagar > 0) {
                resultado.add(new String[] {
                        fornecedor.getNome(),
                        fornecedor.getCNPJ(),
                        fornecedor.getPessoaContato(),
                        fornecedor.getTelefone(),
                        String.format("%.2f", totalPagar).replace(',', '.')
                });
            }
        }

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