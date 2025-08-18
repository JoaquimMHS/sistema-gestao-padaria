// src/main/java/org/padaria/view/TelaCadastroCompra.java
package org.padaria.view;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.padaria.model.Compra;
import org.padaria.service.CompraService;

public class TelaCadastroCompra extends JDialog {

    private final CompraService compraService;
    private final Runnable onSaveCallback;
    private final Compra compraParaEditar;

    private JLabel lblNumeroNFValor;
    private JTextField txtCodigoFornecedor, txtDataCompra, txtCodigoProduto, txtQuantidade;
    private JButton btnSalvar;

    public TelaCadastroCompra(Frame owner, CompraService service, Runnable onSaveCallback, Compra compraParaEditar) {
        super(owner, "Dados da Compra", true);

        this.compraService = service;
        this.onSaveCallback = onSaveCallback;
        this.compraParaEditar = compraParaEditar;

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        adicionarListeners();

        if (compraParaEditar != null) {
            setTitle("Editar Compra");
            preencherCamposParaEdicao();
        } else {
            setTitle("Registrar Nova Compra");
            // A NF não é auto-gerada, o usuário precisa digitar
        }
    }

    private void inicializarComponentes() {
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Número da Nota Fiscal:"));
        lblNumeroNFValor = new JLabel();
        lblNumeroNFValor.setFont(new Font("Arial", Font.BOLD, 14));
        txtCodigoFornecedor = new JTextField();
        txtDataCompra = new JTextField();
        txtCodigoProduto = new JTextField();
        txtQuantidade = new JTextField();

        // No modo de edição, a NF não pode ser alterada.
        if (compraParaEditar != null) {
            lblNumeroNFValor.setText(String.valueOf(compraParaEditar.getNumeroNotaFiscal()));
            panelForm.add(lblNumeroNFValor);
        } else {
            // No modo de cadastro, a NF é um campo de texto para o usuário digitar.
            txtCodigoFornecedor = new JTextField();
            panelForm.add(txtCodigoFornecedor);
        }

        panelForm.add(new JLabel("Cód. Fornecedor:"));
        panelForm.add(txtCodigoFornecedor);
        panelForm.add(new JLabel("Data Compra (YYYY-MM-DD):"));
        panelForm.add(txtDataCompra);
        panelForm.add(new JLabel("Cód. Produto:"));
        panelForm.add(txtCodigoProduto);
        panelForm.add(new JLabel("Quantidade:"));
        panelForm.add(txtQuantidade);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSalvar = new JButton("Salvar");
        panelBotao.add(btnSalvar);
        add(panelBotao, BorderLayout.SOUTH);
    }

    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> salvarCompra());
    }

    private void preencherCamposParaEdicao() {
        lblNumeroNFValor.setText(String.valueOf(compraParaEditar.getNumeroNotaFiscal()));
        txtCodigoFornecedor.setText(String.valueOf(compraParaEditar.getCodigoFornecedor()));
        txtDataCompra.setText(compraParaEditar.getDataCompra().toString());
        txtCodigoProduto.setText(String.valueOf(compraParaEditar.getCodigoProduto()));
        txtQuantidade.setText(String.valueOf(compraParaEditar.getQuantidade()));
    }

    private void salvarCompra() {
        try {
            // Validações
            if (txtCodigoFornecedor.getText().trim().isEmpty() || txtDataCompra.getText().trim().isEmpty() || txtCodigoProduto.getText().trim().isEmpty() || txtQuantidade.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos obrigatórios devem ser preenchidos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int numeroNF = compraParaEditar != null ? compraParaEditar.getNumeroNotaFiscal() : Integer.parseInt(lblNumeroNFValor.getText());
            int codigoFornecedor = Integer.parseInt(txtCodigoFornecedor.getText());
            LocalDate dataCompra = LocalDate.parse(txtDataCompra.getText());
            int codigoProduto = Integer.parseInt(txtCodigoProduto.getText());
            int quantidade = Integer.parseInt(txtQuantidade.getText());

            if (compraParaEditar != null) {
                Compra compraAtualizada = new Compra(numeroNF, codigoFornecedor, dataCompra, codigoProduto, quantidade);
                compraService.atualizar(compraAtualizada);
                JOptionPane.showMessageDialog(this, "Compra atualizada com sucesso!");
            } else {
                Compra novaCompra = new Compra(numeroNF, codigoFornecedor, dataCompra, codigoProduto, quantidade);
                compraService.cadastrar(novaCompra);
                JOptionPane.showMessageDialog(this, "Compra registrada com sucesso!");
            }

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            dispose();

        } catch (NumberFormatException | DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, verifique o formato dos campos numéricos ou da data.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}