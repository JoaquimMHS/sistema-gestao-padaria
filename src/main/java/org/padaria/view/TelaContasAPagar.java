package org.padaria.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
import org.padaria.model.Compra;
import org.padaria.model.Fornecedor;
import org.padaria.model.Produto;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;

public class TelaContasAPagar extends JDialog {

    public TelaContasAPagar(Frame owner, CompraService compraService, FornecedorService fornecedorService,
            ProdutoService produtoService) {
        super(owner, "Contas a Pagar por Fornecedor", true);
        setSize(600, 400);
        setLocationRelativeTo(owner);

        // Lista para armazenar nome do fornecedor e valor acumulado
        List<String> nomesFornecedores = new ArrayList<>();
        List<Double> valoresTotais = new ArrayList<>();

        LocalDate limite = LocalDate.now().minusDays(30);

        List<Compra> compras = compraService.listar();
        for (Compra compra : compras) {

            if (compra.getDataCompra() == null || compra.getDataCompra().isBefore(limite)) {
                continue;
            }

            Fornecedor fornecedor = fornecedorService.buscar(compra.getCodigoFornecedor());
            Produto produto = produtoService.buscar(compra.getCodigoProduto());

            if (fornecedor != null && produto != null) {
                double valorCompra = produto.getValorCusto() * compra.getQuantidade();

                String nome = fornecedor.getNome();
                int indice = nomesFornecedores.indexOf(nome);

                if (indice == -1) {
                    // ainda não existe na lista
                    nomesFornecedores.add(nome);
                    valoresTotais.add(valorCompra);
                } else {
                    // já existe, soma ao valor atual
                    valoresTotais.set(indice, valoresTotais.get(indice) + valorCompra);
                }
            }
        }

        // Ordena fornecedores em ordem alfabética
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < nomesFornecedores.size(); i++)
            indices.add(i);
        indices.sort(Comparator.comparing(i -> nomesFornecedores.get(i)));

        // Monta tabela
        String[] colunas = { "Fornecedor", "Valor Total (R$)" };
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);

        for (int i : indices) {
            tableModel.addRow(new Object[] {
                    nomesFornecedores.get(i),
                    String.format("%.2f", valoresTotais.get(i))
            });
        }

        JTable tabela = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabela);

        setLayout(new BorderLayout());
        add(scrollPane, BorderLayout.CENTER);

        JButton btnFechar = new JButton("Fechar");
        btnFechar.addActionListener(e -> dispose());
        JPanel panelBotoes = new JPanel();
        panelBotoes.add(btnFechar);
        add(panelBotoes, BorderLayout.SOUTH);
    }
}
