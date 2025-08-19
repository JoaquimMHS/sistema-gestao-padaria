package org.padaria.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Collections;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import org.padaria.model.Compra;
import org.padaria.service.CompraService;

public class TelaCompras extends JFrame {

    private final JFrame parent;
    private final CompraService compraService;
    private final String arquivoCSV = "compras.csv";

    private JTable tabelaCompras;
    private DefaultTableModel tableModel;
    private JButton btnAdicionar, btnEditar, btnRemover, btnVoltar;
    private JTextField txtPesquisar;

    public TelaCompras(JFrame parent) {
        this.parent = parent;
        this.compraService = new CompraService();

        try {
            compraService.carregarDados(arquivoCSV);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erro ao carregar dados de compras: " + e.getMessage(), "Erro de Carga", JOptionPane.ERROR_MESSAGE);
        }

        setTitle("Gestão de Compras");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        adicionarListeners();

        carregarDadosNaTabela(compraService.listar());
    }

    private void inicializarComponentes() {
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));
        panelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnVoltar = new JButton("<- Voltar");
        panelTopo.add(btnVoltar, BorderLayout.WEST);


        btnAdicionar = new JButton("Adicionar Compra +");
        panelTopo.add(btnAdicionar, BorderLayout.EAST);

        String[] colunas = {"Nº NF", "Cód. Fornecedor", "Data Compra", "Cód. Produto", "Quantidade"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaCompras = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaCompras);

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

        btnVoltar.addActionListener(e -> {
            salvarEFechar();
        });

        btnAdicionar.addActionListener(e -> {
            TelaCadastroCompra telaCadastro = new TelaCadastroCompra(this, compraService, this::atualizarTabela, null);
            telaCadastro.setVisible(true);
        });
    }

    private void salvarEFechar() {
        try {
            compraService.salvarDados(arquivoCSV);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erro ao salvar dados de compras: " + ex.getMessage(), "Erro de Salvamento", JOptionPane.ERROR_MESSAGE);
        }
        parent.setVisible(true);
        dispose();
    }

    private void carregarDadosNaTabela(List<Compra> compras) {
        tableModel.setRowCount(0);
        for (Compra c : compras) {
            tableModel.addRow(new Object[]{
                    c.getNumeroNotaFiscal(), c.getCodigoFornecedor(), c.getDataCompra(), c.getCodigoProduto(), c.getQuantidade()
            });
        }
    }

    public void atualizarTabela() {
        carregarDadosNaTabela(compraService.listar());
    }

}