package org.padaria.view;

import org.padaria.model.Cliente;
import org.padaria.model.PessoaFisica;
import org.padaria.model.PessoaJuridica;
import org.padaria.model.TipoCliente;
import org.padaria.service.ClienteService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class TelaCadastroCliente extends JDialog {

    private final ClienteService clienteService;
    private final Runnable onSaveCallback;
    private final Cliente clienteParaEditar;

    private JComboBox<TipoCliente> comboTipoCliente;
    private JTextField txtNome, txtEndereco, txtTelefone, txtCpf, txtCnpj, txtInscricaoEstadual;
    private JLabel lblCodigoValor;
    private JButton btnSalvar;
    private JPanel panelCamposDinamicos;
    private CardLayout cardLayout;

    public TelaCadastroCliente(Frame owner, ClienteService service, Runnable onSaveCallback, Cliente clienteParaEditar) {
        super(owner, "Dados do Cliente", true);
        this.clienteService = service;
        this.onSaveCallback = onSaveCallback;
        this.clienteParaEditar = clienteParaEditar;

        setSize(450, 400);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        adicionarListeners();

        if (clienteParaEditar != null) {
            setTitle("Editar Cliente");
            preencherCamposParaEdicao();
        } else {
            setTitle("Cadastrar Novo Cliente");
            int proximoCodigo = 1;
            if (!clienteService.listar().isEmpty()) {
                proximoCodigo = clienteService.listar().stream().mapToInt(Cliente::getCodigo).max().getAsInt() + 1;
            }
            lblCodigoValor.setText(String.valueOf(proximoCodigo));
        }
    }

    private void inicializarComponentes() {
        JPanel panelForm = new JPanel();
        panelForm.setLayout(new BoxLayout(panelForm, BoxLayout.Y_AXIS));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JPanel panelFixo = new JPanel(new GridLayout(0, 2, 5, 10));
        panelFixo.add(new JLabel("Código:"));
        lblCodigoValor = new JLabel();
        panelFixo.add(lblCodigoValor);

        panelFixo.add(new JLabel("Nome Completo:"));
        txtNome = new JTextField();
        panelFixo.add(txtNome);

        panelFixo.add(new JLabel("Endereço:"));
        txtEndereco = new JTextField();
        panelFixo.add(txtEndereco);

        panelFixo.add(new JLabel("Telefone:"));
        txtTelefone = new JTextField();
        panelFixo.add(txtTelefone);

        panelFixo.add(new JLabel("Tipo de Cliente:"));
        comboTipoCliente = new JComboBox<>(TipoCliente.values());
        panelFixo.add(comboTipoCliente);
        panelForm.add(panelFixo);

        // Painel para campos que mudam (CPF ou CNPJ/IE)
        cardLayout = new CardLayout();
        panelCamposDinamicos = new JPanel(cardLayout);

        // Painel Pessoa Física
        JPanel panelPF = new JPanel(new GridLayout(0, 2, 5, 10));
        panelPF.add(new JLabel("CPF:"));
        txtCpf = new JTextField();
        panelPF.add(txtCpf);
        panelCamposDinamicos.add(panelPF, TipoCliente.PESSOA_FISICA.toString());

        // Painel Pessoa Jurídica
        JPanel panelPJ = new JPanel(new GridLayout(0, 2, 5, 10));
        panelPJ.add(new JLabel("CNPJ:"));
        txtCnpj = new JTextField();
        panelPJ.add(txtCnpj);
        panelPJ.add(new JLabel("Inscrição Estadual:"));
        txtInscricaoEstadual = new JTextField();
        panelPJ.add(txtInscricaoEstadual);
        panelCamposDinamicos.add(panelPJ, TipoCliente.PESSOA_JURIDICA.toString());
        
        panelForm.add(Box.createRigidArea(new Dimension(0, 15))); // Espaçamento
        panelForm.add(panelCamposDinamicos);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSalvar = new JButton("Salvar");
        panelBotao.add(btnSalvar);
        add(panelBotao, BorderLayout.SOUTH);
    }

    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> salvarCliente());

        comboTipoCliente.addActionListener(e -> {
            TipoCliente selecionado = (TipoCliente) comboTipoCliente.getSelectedItem();
            cardLayout.show(panelCamposDinamicos, selecionado.toString());
        });
    }
    
    private void preencherCamposParaEdicao() {
        lblCodigoValor.setText(String.valueOf(clienteParaEditar.getCodigo()));
        txtNome.setText(clienteParaEditar.getNome());
        txtEndereco.setText(clienteParaEditar.getEndereco());
        txtTelefone.setText(clienteParaEditar.getTelefone());
        comboTipoCliente.setSelectedItem(clienteParaEditar.getTipo());

        if (clienteParaEditar instanceof PessoaFisica) {
            txtCpf.setText(((PessoaFisica) clienteParaEditar).getCpf());
        } else if (clienteParaEditar instanceof PessoaJuridica) {
            txtCnpj.setText(((PessoaJuridica) clienteParaEditar).getCnpj());
            txtInscricaoEstadual.setText(String.valueOf(((PessoaJuridica) clienteParaEditar).getInscricaoEstadual()));
        }
        comboTipoCliente.setEnabled(false); // Impede a troca de tipo na edição
    }

    private void salvarCliente() {
        if (txtNome.getText().trim().isEmpty() || txtEndereco.getText().trim().isEmpty() || txtTelefone.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int codigo = Integer.parseInt(lblCodigoValor.getText());
            String nome = txtNome.getText();
            String endereco = txtEndereco.getText();
            String telefone = txtTelefone.getText();
            TipoCliente tipo = (TipoCliente) comboTipoCliente.getSelectedItem();

            Cliente novoCliente;

            if (tipo == TipoCliente.PESSOA_FISICA) {
                String cpf = txtCpf.getText();
                novoCliente = new PessoaFisica(codigo, nome, endereco, telefone, LocalDate.now(), cpf);
            } else { // Pessoa Jurídica
                String cnpj = txtCnpj.getText();
                int inscricao = Integer.parseInt(txtInscricaoEstadual.getText());
                novoCliente = new PessoaJuridica(codigo, nome, endereco, telefone, LocalDate.now(), cnpj, inscricao);
            }

            if (!novoCliente.isValid()) {
                JOptionPane.showMessageDialog(this, "CPF ou CNPJ inválido. Verifique os dados.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (clienteParaEditar != null) {
                clienteService.atualizar(novoCliente);
                JOptionPane.showMessageDialog(this, "Cliente atualizado com sucesso!");
            } else {
                clienteService.cadastrar(novoCliente);
                JOptionPane.showMessageDialog(this, "Cliente cadastrado com sucesso!");
            }

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Verifique se os campos numéricos (Inscrição Estadual) foram preenchidos corretamente.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}