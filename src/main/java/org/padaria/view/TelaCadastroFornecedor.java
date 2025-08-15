// Salve em: org/padaria/view/TelaCadastroFornecedor.java
package org.padaria.view;

import org.padaria.model.Fornecedor;
import org.padaria.service.FornecedorService;

import javax.swing.*;
import java.awt.*;

public class TelaCadastroFornecedor extends JDialog {

    // Dependências e Callbacks
    private final FornecedorService fornecedorService;
    private final Runnable onSaveCallback; // Para atualizar a tabela da tela principal
    private final Fornecedor fornecedorParaEditar; // Se for nulo, é cadastro. Senão, é edição.

    // Componentes do Formulário
    private JLabel lblCodigoValor;
    private JTextField txtNome, txtEndereco, txtTelefone, txtCnpj, txtPessoaContato;
    private JButton btnSalvar;

    public TelaCadastroFornecedor(Frame owner, FornecedorService service, Runnable onSaveCallback, Fornecedor fornecedorParaEditar) {
        super(owner, "Dados do Fornecedor", true); // O 'true' torna o diálogo modal

        this.fornecedorService = service;
        this.onSaveCallback = onSaveCallback;
        this.fornecedorParaEditar = fornecedorParaEditar;

        setSize(450, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        adicionarListeners();

        // Decide se a tela está em modo de EDIÇÃO ou CADASTRO
        if (fornecedorParaEditar != null) {
            setTitle("Editar Fornecedor");
            preencherCamposParaEdicao();
        } else {
            setTitle("Cadastrar Novo Fornecedor");
            // Define o próximo código disponível
            int proximoCodigo = 1;
            if (!fornecedorService.listar().isEmpty()) {
                proximoCodigo = fornecedorService.listar().stream().mapToInt(Fornecedor::getCodigo).max().getAsInt() + 1;
            }
            lblCodigoValor.setText(String.valueOf(proximoCodigo));
        }
    }

    private void inicializarComponentes() {
        // Painel do formulário com layout de grade
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Código:"));
        lblCodigoValor = new JLabel();
        lblCodigoValor.setFont(new Font("Arial", Font.BOLD, 14));
        panelForm.add(lblCodigoValor);

        panelForm.add(new JLabel("Nome:"));
        txtNome = new JTextField();
        panelForm.add(txtNome);

        panelForm.add(new JLabel("Endereço:"));
        txtEndereco = new JTextField();
        panelForm.add(txtEndereco);

        panelForm.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        panelForm.add(txtTelefone);

        panelForm.add(new JLabel("CNPJ:"));
        txtCnpj = new JTextField();
        panelForm.add(txtCnpj);

        panelForm.add(new JLabel("Pessoa de Contato:"));
        txtPessoaContato = new JTextField();
        panelForm.add(txtPessoaContato);

        add(panelForm, BorderLayout.CENTER);

        // Painel para o botão salvar
        JPanel panelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSalvar = new JButton("Salvar");
        panelBotao.add(btnSalvar);
        add(panelBotao, BorderLayout.SOUTH);
    }

    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> salvarFornecedor());
    }

    private void preencherCamposParaEdicao() {
        lblCodigoValor.setText(String.valueOf(fornecedorParaEditar.getCodigo()));
        txtNome.setText(fornecedorParaEditar.getNome());
        txtEndereco.setText(fornecedorParaEditar.getEndereco());
        txtTelefone.setText(fornecedorParaEditar.getTelefone());
        txtCnpj.setText(fornecedorParaEditar.getCNPJ());
        txtPessoaContato.setText(fornecedorParaEditar.getPessoaContato());
    }

    private void salvarFornecedor() {
        // Validação simples
        if (txtNome.getText().trim().isEmpty() || txtCnpj.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Os campos 'Nome' e 'CNPJ' são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Coleta os dados dos campos
            String nome = txtNome.getText();
            String endereco = txtEndereco.getText();
            String telefone = txtTelefone.getText();
            String cnpj = txtCnpj.getText();
            String pessoaContato = txtPessoaContato.getText();

            if (fornecedorParaEditar != null) { // Modo Edição
                Fornecedor fornecedorAtualizado = new Fornecedor(fornecedorParaEditar.getCodigo(), nome, endereco, telefone, cnpj, pessoaContato);
                fornecedorService.atualizar(fornecedorAtualizado);
                JOptionPane.showMessageDialog(this, "Fornecedor atualizado com sucesso!");
            } else { // Modo Cadastro
                int novoCodigo = Integer.parseInt(lblCodigoValor.getText());
                Fornecedor novoFornecedor = new Fornecedor(novoCodigo, nome, endereco, telefone, cnpj, pessoaContato);
                fornecedorService.cadastrar(novoFornecedor);
                JOptionPane.showMessageDialog(this, "Fornecedor cadastrado com sucesso!");
            }

            // Executa o callback para atualizar a tabela na tela principal
            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            dispose(); // Fecha a janela de diálogo

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro ao salvar: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}