package org.padaria.view;

import org.padaria.model.Cliente;
import org.padaria.model.PessoaFisica;
import org.padaria.model.PessoaJuridica;
import org.padaria.service.ClienteService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;
import java.util.stream.Collectors;

public class TelaCliente extends JFrame {

    private final ClienteService clienteService;
    private final String arquivoCSV = "clientes.csv";

    private JTable tabelaClientes;
    private DefaultTableModel tableModel;
    private JButton btnAdicionar, btnEditar, btnRemover;
    private JTextField txtPesquisar;

    public TelaCliente() {
        clienteService = new ClienteService(arquivoCSV);

        setTitle("Clientes");
        setSize(900, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        inicializarComponentes();
        adicionarListeners();

        atualizarTabela();
    }

    private void inicializarComponentes() {
        JPanel panelTopo = new JPanel(new BorderLayout(10, 10));
        panelTopo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtPesquisar = new JTextField();
        txtPesquisar.setBorder(BorderFactory.createTitledBorder("Pesquisar por Nome"));
        panelTopo.add(txtPesquisar, BorderLayout.CENTER);

        btnAdicionar = new JButton("Adicionar Cliente +");
        btnAdicionar.setBackground(new Color(21, 139, 21));
        btnAdicionar.setForeground(Color.WHITE);
        panelTopo.add(btnAdicionar, BorderLayout.EAST);

        String[] colunas = {"Código", "Nome", "Tipo", "CPF/CNPJ", "Telefone", "Endereço"};
        tableModel = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        tabelaClientes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaClientes);

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.LEFT));
        btnEditar = new JButton("Editar Selecionado");
        btnRemover = new JButton("Remover Selecionado");
        panelBotoes.add(btnEditar);
        panelBotoes.add(btnRemover);

        setLayout(new BorderLayout());
        add(panelTopo, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(panelBotoes, BorderLayout.SOUTH);
    }

    private void adicionarListeners() {
        // Salva os dados no arquivo ao fechar a janela
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                // O ClienteService já salva automaticamente após cada operação.
                // Se não salvasse, a chamada de salvamento viria aqui.
            }
        });

        btnAdicionar.addActionListener(e -> {
            TelaCadastroCliente telaCadastro = new TelaCadastroCliente(this, clienteService, this::atualizarTabela, null);
            telaCadastro.setVisible(true);
        });

        btnEditar.addActionListener(e -> editarCliente());
        btnRemover.addActionListener(e -> removerCliente());

        txtPesquisar.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { filtrarTabela(); }
            @Override
            public void removeUpdate(DocumentEvent e) { filtrarTabela(); }
            @Override
            public void changedUpdate(DocumentEvent e) { filtrarTabela(); }
        });
    }

    private void carregarDadosNaTabela(List<Cliente> clientes) {
        tableModel.setRowCount(0);
        for (Cliente c : clientes) {
            String tipo = "";
            String documento = "";
            if (c instanceof PessoaFisica) {
                tipo = "Pessoa Física";
                documento = ((PessoaFisica) c).getCpf();
            } else if (c instanceof PessoaJuridica) {
                tipo = "Pessoa Jurídica";
                documento = ((PessoaJuridica) c).getCnpj();
            }
            tableModel.addRow(new Object[]{
                    c.getCodigo(),
                    c.getNome(),
                    tipo,
                    documento,
                    c.getTelefone(),
                    c.getEndereco()
            });
        }
    }

    public void atualizarTabela() {
        carregarDadosNaTabela(clienteService.listar());
    }

    private void filtrarTabela() {
        String textoBusca = txtPesquisar.getText().trim().toLowerCase();
        if (textoBusca.isEmpty()) {
            atualizarTabela();
        } else {
            List<Cliente> todosClientes = clienteService.listar();
            List<Cliente> clientesFiltrados = todosClientes.stream()
                    .filter(c -> c.getNome().toLowerCase().contains(textoBusca))
                    .collect(Collectors.toList());
            carregarDadosNaTabela(clientesFiltrados);
        }
    }

    private void editarCliente() {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um cliente na tabela para editar.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int codigo = (int) tableModel.getValueAt(selectedRow, 0);
        Cliente clienteParaEditar = clienteService.buscar(codigo);

        TelaCadastroCliente telaEdicao = new TelaCadastroCliente(this, clienteService, this::atualizarTabela, clienteParaEditar);
        telaEdicao.setVisible(true);
    }

    private void removerCliente() {
        int selectedRow = tabelaClientes.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Por favor, selecione um cliente na tabela para remover.", "Aviso", JOptionPane.WARNING_MESSAGE);
            return;
        }
        int resposta = JOptionPane.showConfirmDialog(this, "Tem certeza que deseja remover o cliente selecionado?", "Confirmação de Remoção", JOptionPane.YES_NO_OPTION);
        if (resposta == JOptionPane.YES_OPTION) {
            int codigo = (int) tableModel.getValueAt(selectedRow, 0);
            if (clienteService.remover(codigo)) {
                atualizarTabela();
                JOptionPane.showMessageDialog(this, "Cliente removido com sucesso!");
            }
        }
    }
}