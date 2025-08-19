package org.padaria.report;

import org.padaria.model.Cliente;
import org.padaria.model.ModoPagamento;
import org.padaria.model.PessoaFisica;
import org.padaria.model.PessoaJuridica;
import org.padaria.model.Produto;
import org.padaria.model.TipoCliente;
import org.padaria.model.Venda;
import org.padaria.service.ClienteService;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;
import org.padaria.util.CSVUtil;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RelatorioContasReceber implements IRelatorio {

    private final ClienteService clienteService;
    private final VendaService vendaService;
    private final ProdutoService produtoService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public RelatorioContasReceber(ClienteService clienteService, VendaService vendaService,
            ProdutoService produtoService) {
        this.clienteService = clienteService;
        this.vendaService = vendaService;
        this.produtoService = produtoService;
    }

    @Override
    public void gerar(String nomeArquivo) {
        List<String[]> dados = processarDados();
        String[] cabecalho = getCabecalho();
        CSVUtil.escreverArquivoCSV(nomeArquivo, dados, cabecalho);
    }

    @Override
    public List<String[]> processarDados() {
        List<Venda> vendasFiado = vendaService.listarVendasPorPagamento(ModoPagamento.FIADO);
        List<Cliente> clientes = clienteService.listar();
        List<Produto> produtos = produtoService.listar();

        List<String[]> linhasRelatorio = new ArrayList<>();

        // Para cada cliente, calcula o total a receber
        for (Cliente cliente : clientes) {
            double totalReceber = 0.0;

            // Encontra todas as vendas fiado deste cliente
            for (Venda venda : vendasFiado) {
                if (venda.getCodigoCliente() == cliente.getCodigo()) {
                    // Encontra o produto correspondente
                    for (Produto produto : produtos) {
                        if (produto.getCodigo() == venda.getCodigoProduto()) {
                            totalReceber += produto.calcularValorVenda() * venda.getQuantidade();
                            break; // Produto encontrado, sai do loop interno
                        }
                    }
                }
            }

            // Só adiciona se houver valor a receber
            if (totalReceber > 0) {
                String[] linha = new String[6];
                linha[0] = cliente.getNome();

                // Determina tipo e documento do cliente
                if (cliente.getTipo() == TipoCliente.PESSOA_FISICA && cliente instanceof PessoaFisica) {
                    linha[1] = "F";
                    linha[2] = ((PessoaFisica) cliente).getCpf();
                } else if (cliente.getTipo() == TipoCliente.PESSOA_JURIDICA && cliente instanceof PessoaJuridica) {
                    linha[1] = "J";
                    linha[2] = ((PessoaJuridica) cliente).getCnpj();
                } else {
                    linha[1] = "N/A";
                    linha[2] = "N/A";
                }

                linha[3] = cliente.getTelefone();
                linha[4] = cliente.getDataCadastro().format(DATE_FORMATTER);
                linha[5] = String.format("%.2f", totalReceber).replace(",", ".");

                linhasRelatorio.add(linha);
            }
        }

        // Ordena por nome do cliente
        linhasRelatorio.sort(Comparator.comparing(linha -> linha[0]));

        return linhasRelatorio;
    }

    @Override
    public String[] getCabecalho() {
        return new String[] {
                "nome do cliente",
                "tipo do cliente",
                "cpf/cnpj do cliente",
                "telefone",
                "data de cadastro",
                "valor total a receber"
        };
    }
}