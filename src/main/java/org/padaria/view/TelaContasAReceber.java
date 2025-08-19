package org.padaria.view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;
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

public class TelaContasAReceber extends JDialog {

    public TelaContasAReceber(Frame owner, VendaService vendaService, ClienteService clienteService,
            ProdutoService produtoService) {
        super(owner, "Contas a Receber (Fiado - Últimos 30 dias)", true);
        setSize(750, 400);
        setLocationRelativeTo(owner);

        List<Cliente> clientes = new ArrayList<>();
        List<Double> totais = new ArrayList<>();

        // Data limite (30 dias atrás)
        LocalDate limite = LocalDate.now().minusDays(30);

        List<Venda> vendas = vendaService.listar();
        for (Venda venda : vendas) {
            if (venda.getModoPagamento() != ModoPagamento.FIADO)
                continue;
            if (venda.getDataVenda() == null || venda.getDataVenda().isBefore(limite))
                continue;

            Cliente cliente = clienteService.buscar(venda.getCodigoCliente());
            Produto produto = produtoService.buscar(venda.getCodigoProduto());

            if (cliente != null && produto != null) {
                double valorVenda = produto.getValorCusto() * venda.getQuantidade();

                int indice = clientes.indexOf(cliente);
                if (indice == -1) {
                    clientes.add(cliente);
                    totais.add(valorVenda);
                } else {
                    totais.set(indice, totais.get(indice) + valorVenda);
                }
            }
        }

        // ordenar por nome do cliente
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < clientes.size(); i++)
            indices.add(i);
        indices.sort(Comparator.comparing(i -> clientes.get(i).getNome()));

        // monta tabela
        String[] colunas = { "Nome", "CPF/CNPJ", "Telefone", "Total a Pagar (R$)" };
        DefaultTableModel tableModel = new DefaultTableModel(colunas, 0);

        for (int i : indices) {
            Cliente c = clientes.get(i);

            // Decide o documento com base na subclasse
            String documento = "";
            if (c instanceof PessoaFisica) {
                documento = ((PessoaFisica) c).getCpf();
            } else if (c instanceof PessoaJuridica) {
                documento = ((PessoaJuridica) c).getCnpj();
            }

            tableModel.addRow(new Object[] {
                    c.getNome(),
                    documento,
                    c.getTelefone(),
                    String.format("%.2f", totais.get(i))
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
