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
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        Map<Integer, Double> totaisPorCliente = new HashMap<>();

        // Busca vendas fiado usando o enum correto
        List<Venda> vendasFiado = vendaService.listarVendasPorPagamento(ModoPagamento.FIADO);

        // Calcula o total por cliente
        for (Venda venda : vendasFiado) {
            Produto produto = produtoService.buscar(venda.getCodigoProduto());
            if (produto != null) {
                // Usa o método correto para calcular valor de venda
                double valorVenda = produto.calcularValorVenda() * venda.getQuantidade();
                totaisPorCliente.merge(venda.getCodigoCliente(), valorVenda, Double::sum);
            }
        }

        // Monta as linhas do relatório
        List<String[]> linhasRelatorio = new ArrayList<>();
        for (Map.Entry<Integer, Double> entry : totaisPorCliente.entrySet()) {
            Cliente cliente = clienteService.buscar(entry.getKey());
            if (cliente != null) {
                String[] linha = new String[6];
                linha[0] = cliente.getNome();

                // Determina tipo e documento do cliente com cast seguro
                if (cliente.getTipo() == TipoCliente.PESSOA_FISICA && cliente instanceof PessoaFisica) {
                    linha[1] = "F";
                    linha[2] = ((PessoaFisica) cliente).getCpf();
                } else if (cliente.getTipo() == TipoCliente.PESSOA_JURIDICA && cliente instanceof PessoaJuridica) {
                    linha[1] = "J";
                    linha[2] = ((PessoaJuridica) cliente).getCnpj();
                } else {
                    // Fallback para casos inesperados
                    linha[1] = "N/A";
                    linha[2] = "N/A";
                }

                linha[3] = cliente.getTelefone();
                linha[4] = cliente.getDataCadastro().format(DATE_FORMATTER);
                linha[5] = String.format("%.2f", entry.getValue()).replace(",", ".");

                linhasRelatorio.add(linha);
            }
        }

        // Ordena por nome do cliente
        linhasRelatorio.sort(Comparator.comparing(linha -> linha[0]));

        return linhasRelatorio;
    }

    @Override
    public String[] getCabecalho() {
        return new String[]{
                "nome do cliente",
                "tipo do cliente",
                "cpf/cnpj do cliente",
                "telefone",
                "data de cadastro",
                "valor total a receber"
        };
    }
}