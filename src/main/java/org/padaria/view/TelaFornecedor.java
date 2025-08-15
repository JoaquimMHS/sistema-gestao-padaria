package org.padaria.view;

import org.padaria.model.Fornecedor;
import org.padaria.service.FornecedorService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

public class TelaFornecedor extends JFrame {

    private final JFrame parent;
    private final FornecedorService fornecedorService;
    private final String arquivoCSV = "fornecedores.csv";

    // Componentes da Tela
    private JTable tabelaFornecedores;
    private DefaultTableModel tableModel;
    private JButton btnAdicionar, btnEditar, btnRemover, btnVoltar;

    public TelaFornecedor(JFrame parent) {
        this.parent = parent;
        this.fornecedorService = new FornecedorService();
        this.fornecedorService.carregarDados(arquivoCSV);

        setTitle("Gerenciamento de Fornecedores");
        setSize(850, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        adicionarListeners();

        atualizarTabela();
    }

    private void inicializarComponentes() {
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));
        panelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        btnVoltar = new JButton("<- Voltar ao Menu");
        panelTopo.add(btnVoltar, BorderLayout.WEST);

        btnAdicionar = new JButton("Adicionar Fornecedor +");
        panelTopo.add(btnAdicionar, BorderLayout.EAST);

        add(panelTopo, BorderLayout.NORTH);

        // Configuração da tabela
        String[] colunas = {"Código", "Nome", "CNPJ", "Telefone", "Contato"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        tabelaFornecedores = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaFornecedores);
        add(scrollPane, BorderLayout.CENTER);

        // Painel inferior com botões de ação
        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnEditar = new JButton("Editar Selecionado");
        btnRemover = new JButton("Remover Selecionado");
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnRemover);
        add(panelBotoes, BorderLayout.SOUTH);
    }

    private void adicionarListeners() {
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                fornecedorService.salvarDados(arquivoCSV);
                parent.setVisible(true);
            }
        });

        btnVoltar.addActionListener(e -> {
            fornecedorService.salvarDados(arquivoCSV);
            parent.setVisible(true);
            dispose();
        });

        // Ações dos botões
        btnAdicionar.addActionListener(e -> abrirTelaDeCadastro(null));
        btnEditar.addActionListener(e -> editarFornecedor());
        btnRemover.addActionListener(e -> removerFornecedor());
    }

    private void carregarDadosNaTabela(List<Fornecedor> fornecedores) {
        tableModel.setRowCount(0); // Limpa a tabela
        for (Fornecedor f : fornecedores) {
            tableModel.addRow(new Object[]{
                    f.getCodigo(),
                    f.getNome(),
                    f.getCNPJ(),
                    f.getTelefone(),
                    f.getPessoaContato()
            });
        }
    }

    public void atualizarTabela() {
        carregarDadosNaTabela(fornecedorService.listar());
    }

    private void abrirTelaDeCadastro(Fornecedor fornecedor) {
        // 'this' é a janela pai, 'fornecedorService' é o serviço,
        // 'this::atualizarTabela' é o callback mágico para atualizar a tabela,
        // e 'fornecedor' é o objeto a ser editado (ou null se for cadastro)
        TelaCadastroFornecedor telaCadastro = new TelaCadastroFornecedor(this, fornecedorService, this::atualizarTabela, fornecedor);
        telaCadastro.setVisible(true);
    }

    private void editarFornecedor() {
        int selectedRow = tabelaFornecedores.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um fornecedor na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int codigo = (int) tableModel.getValueAt(selectedRow, 0);
        Fornecedor fornecedorParaEditar = fornecedorService.buscar(codigo);
        abrirTelaDeCadastro(fornecedorParaEditar);
    }

    private void removerFornecedor() {
        int selectedRow = tabelaFornecedores.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um fornecedor para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o fornecedor selecionado?", "Confirmação", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            int codigo = (int) tableModel.getValueAt(selectedRow, 0);
            fornecedorService.remover(codigo);
            atualizarTabela();
            JOptionPane.showMessageDialog(this, "Fornecedor removido com sucesso!");
        }
    }
}