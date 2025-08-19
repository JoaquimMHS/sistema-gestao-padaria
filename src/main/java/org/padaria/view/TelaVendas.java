// src/main/java/org/padaria/view/TelaVendas.java
package org.padaria.view;

import org.padaria.model.Venda;
import org.padaria.service.ClienteService;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class TelaVendas extends JFrame {

    private final JFrame parent;
    private final VendaService vendaService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final String arquivoCSV = "vendas.csv";
    private final String arquivoProdutosCSV = "produtos.csv";

    private JTable tabelaVendas;
    private DefaultTableModel tableModel;

    // Construtor que recebe os serviços da tela principal (Injeção de Dependência)
    public TelaVendas(JFrame parent) {
        String arquivoClientes = "clientes.csv";
        this.parent = parent;
        vendaService = new VendaService();
        clienteService = new ClienteService(arquivoClientes);
        produtoService = new ProdutoService();

        // Carrega os dados do arquivo CSV ao abrir a tela
        try {
            vendaService.carregarDados(arquivoCSV);
            produtoService.carregarDados(arquivoProdutosCSV);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados de vendas: " + e.getMessage(), "Erro de Carga",
                    JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Gestão de Vendas");
        setSize(850, 500);
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        adicionarListeners();

        // Popula a tabela com os dados carregados
        atualizarTabela();
    }

    private void inicializarComponentes() {
        // Painel superior com botões de ação
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));
        panelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnVoltar = new JButton("<- Voltar");
        panelTopo.add(btnVoltar, BorderLayout.WEST);

        JButton btnAdicionar = new JButton("Adicionar Venda +");
        panelTopo.add(btnAdicionar, BorderLayout.EAST);

        // Ação do botão voltar
        btnVoltar.addActionListener(e -> salvarEFechar());

        // Ação do botão adicionar: abre a tela de cadastro
        btnAdicionar.addActionListener(e -> {
            TelaCadastroVenda telaCadastro = new TelaCadastroVenda(this, vendaService, clienteService, produtoService,
                    this::atualizarTabela);
            telaCadastro.setVisible(true);
        });

        // Configuração da tabela de vendas
        String[] colunas = { "Cód. Cliente", "Data Venda", "Cód. Produto", "Quantidade", "Pagamento" };
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Células não são editáveis
            }
        };
        tabelaVendas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaVendas);

        // Adiciona os componentes à janela
        setLayout(new BorderLayout());
        add(panelTopo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private void adicionarListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                salvarEFechar();
            }
        });
    }

    private void salvarEFechar() {
        try {
            vendaService.salvarDados(arquivoCSV);
            produtoService.salvarDados(arquivoProdutosCSV);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados: " + ex.getMessage(), "Erro de Salvamento",
                    JOptionPane.ERROR_MESSAGE);
        }
        // parent.setVisible(true); // Descomente se quiser que a tela anterior
        // reapareça
        dispose(); // Fecha esta janela
    }

    // Carrega a lista de vendas do serviço para a tabela na UI
    private void carregarDadosNaTabela(List<Venda> vendas) {
        tableModel.setRowCount(0); // Limpa a tabela
        if (vendas == null)
            return;

        for (Venda v : vendas) {
            tableModel.addRow(new Object[] {
                    v.getCodigoCliente() != null ? v.getCodigoCliente() : "N/A",
                    v.getDataVenda(),
                    v.getCodigoProduto(),
                    v.getQuantidade(),
                    v.getModoPagamento().name()
            });
        }
    }

    // Método público que pode ser chamado pela TelaCadastroVenda para atualizar a
    // lista
    public void atualizarTabela() {
        carregarDadosNaTabela(vendaService.listar());
    }
}