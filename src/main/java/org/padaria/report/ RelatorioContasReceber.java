// package org.padaria.report;

// import org.padaria.model.Cliente;
// import org.padaria.model.PessoaFisica;
// import org.padaria.model.PessoaJuridica;
// import org.padaria.model.Produto;
// import org.padaria.model.TipoCliente;
// import org.padaria.model.Venda;
// import org.padaria.service.ClienteService;
// import org.padaria.service.ProdutoService;
// import org.padaria.service.VendaService;
// import org.padaria.util.CSVUtil;

// import java.time.format.DateTimeFormatter;
// import java.util.ArrayList;
// import java.util.Comparator;
// import java.util.HashMap;
// import java.util.List;
// import java.util.Map;

// /**
// * Classe responsável por gerar o relatório de contas a receber por cliente.
// */
// public class RelatorioContasReceber implements IRelatorio {

// private final ClienteService clienteService;
// private final VendaService vendaService;
// private final ProdutoService produtoService;
// private static final DateTimeFormatter DATE_FORMATTER =
// DateTimeFormatter.ofPattern("dd/MM/yyyy");

// public RelatorioContasReceber(ClienteService clienteService, VendaService
// vendaService,
// ProdutoService produtoService) {
// this.clienteService = clienteService;
// this.vendaService = vendaService;
// this.produtoService = produtoService;
// }

// @Override
// public void gerar(String nomeArquivo) {
// List<String[]> dados = processarDados();
// String[] cabecalho = getCabecalho();
// CSVUtil.escreverArquivoCSV(nomeArquivo, dados, cabecalho);
// }

// @Override
// public List<String[]> processarDados() {
// Map<Integer, Double> totaisPorCliente = new HashMap<>();
// List<Venda> vendasFiado = vendaService.listarVendasPorModoDePagamento("F");

// for (Venda venda : vendasFiado) {
// Produto produto = produtoService.buscar(venda.getCodigoProduto());
// if (produto != null) {
// double valorVenda = produto.getValorDeVenda() * venda.getQuantidade();
// totaisPorCliente.merge(venda.getCodigoCliente(), valorVenda, Double::sum);
// }
// }

// List<String[]> linhasRelatorio = new ArrayList<>();
// for (Map.Entry<Integer, Double> entry : totaisPorCliente.entrySet()) {
// Cliente cliente = clienteService.buscar(entry.getKey());
// if (cliente != null) {
// String[] linha = new String[6];
// linha[0] = cliente.getNome();
// linha[2] = "";

// if (cliente.getTipo() == TipoCliente.PESSOA_FISICA) {
// linha[1] = "F";
// linha[2] = ((PessoaFisica) cliente).getCpf();
// } else if (cliente.getTipo() == TipoCliente.PESSOA_JURIDICA) {
// linha[1] = "J";
// linha[2] = ((PessoaJuridica) cliente).getCnpj();
// }

// linha[3] = cliente.getTelefone();
// linha[4] = cliente.getDataCadastro().format(DATE_FORMATTER);
// // A formatação de valores pode ser feita com a biblioteca padrão do Java,
// // já que FormatUtil e NumberUtil estão vazios.
// linha[5] = String.format("%.2f", entry.getValue()).replace(",", ".");

// linhasRelatorio.add(linha);
// }
// }

// // Ordenado por nome do fornecedor [cite: 188]
// linhasRelatorio.sort(Comparator.comparing(linha -> linha[0]));

// return linhasRelatorio;
// }

// @Override
// public String[] getCabecalho() {
// // Cabeçalho conforme o arquivo TrabalhoPratico-POO-V2.pdf [cite: 186]
// return new String[] {
// "nome do cliente",
// "tipo do cliente",
// "cpf/cnpj do cliente",
// "telefone",
// "data de cadastro",
// "valor total a receber"
// };
// }
// }