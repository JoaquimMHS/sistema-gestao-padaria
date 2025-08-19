package org.padaria.view;

import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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

    private JTextField txtNumeroNF, txtCodigoFornecedor, txtDataCompra, txtCodigoProduto, txtQuantidade;
    private JButton btnSalvar;

    public TelaCadastroCompra(Frame owner, CompraService service, Runnable onSaveCallback, Compra compraParaEditar) {
        super(owner, true);

        this.compraService = service;
        this.onSaveCallback = onSaveCallback;
        this.compraParaEditar = compraParaEditar;

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        adicionarListeners();

            setTitle("Registrar Nova Compra");
    }

    private void inicializarComponentes() {
        JPanel panelForm = new JPanel(new GridLayout(5, 2, 5, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        txtNumeroNF = new JTextField();
        txtNumeroNF.setFont(new Font("Arial", Font.BOLD, 14));
        txtCodigoFornecedor = new JTextField();
        txtDataCompra = new JTextField();
        txtCodigoProduto = new JTextField();
        txtQuantidade = new JTextField();

        panelForm.add(new JLabel("Número da Nota Fiscal:"));
        panelForm.add(txtNumeroNF);

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

    private void preencherFormularioParaEdicao() {
        txtNumeroNF.setText(String.valueOf(compraParaEditar.getNumeroNotaFiscal()));
        txtNumeroNF.setEnabled(false);
        txtCodigoFornecedor.setText(String.valueOf(compraParaEditar.getCodigoFornecedor()));
        txtDataCompra.setText(compraParaEditar.getDataCompra().format(DateTimeFormatter.ISO_LOCAL_DATE));
        txtCodigoProduto.setText(String.valueOf(compraParaEditar.getCodigoProduto()));
        txtQuantidade.setText(String.valueOf(compraParaEditar.getQuantidade()));
    }

    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> salvarCompra());
    }

    private void salvarCompra() {
        try {
            // Validações
            if (txtNumeroNF.getText().trim().isEmpty() || txtCodigoFornecedor.getText().trim().isEmpty() || txtDataCompra.getText().trim().isEmpty() || txtCodigoProduto.getText().trim().isEmpty() || txtQuantidade.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos devem ser preenchidos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int numeroNF = Integer.parseInt(txtNumeroNF.getText());
            int codigoFornecedor = Integer.parseInt(txtCodigoFornecedor.getText());
            LocalDate dataCompra = LocalDate.parse(txtDataCompra.getText());
            int codigoProduto = Integer.parseInt(txtCodigoProduto.getText());
            int quantidade = Integer.parseInt(txtQuantidade.getText());

                Compra novaCompra = new Compra(numeroNF, codigoFornecedor, dataCompra, codigoProduto, quantidade);
                compraService.cadastrar(novaCompra);
                JOptionPane.showMessageDialog(this, "Compra registrada com sucesso!");

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, verifique se os campos numéricos contêm apenas números.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "O formato da data deve ser YYYY-MM-DD (ex: 2025-08-19).", "Erro de Formato de Data", JOptionPane.ERROR_MESSAGE);
        }
    }
}