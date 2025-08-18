// src/main/java/org/padaria/view/TelaCadastroVenda.java
package org.padaria.view;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.padaria.model.ModoPagamento;
import org.padaria.model.Venda;
import org.padaria.service.VendaService;

public class TelaCadastroVenda extends JDialog {

    private final VendaService vendaService;
    private final Runnable onSaveCallback;
    private final Venda vendaParaEditar;

    private JLabel lblCodigoValor;
    private JTextField txtCodigoCliente, txtDataVenda, txtCodigoProduto, txtQuantidade, txtModoPagamento;
    private JButton btnSalvar;

    public TelaCadastroVenda(Frame owner, VendaService service, Runnable onSaveCallback, Venda vendaParaEditar) {
        super(owner, "Dados da Venda", true);

        this.vendaService = service;
        this.onSaveCallback = onSaveCallback;
        this.vendaParaEditar = vendaParaEditar;

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        adicionarListeners();

        if (vendaParaEditar != null) {
            setTitle("Editar Venda");
            preencherCamposParaEdicao();
        } else {
            setTitle("Registrar Nova Venda");
        }
    }

    private void inicializarComponentes() {
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Código:"));
        lblCodigoValor = new JLabel("Auto-gerado");
        panelForm.add(lblCodigoValor);

        panelForm.add(new JLabel("Cód. Cliente (opcional):"));
        txtCodigoCliente = new JTextField();
        panelForm.add(txtCodigoCliente);

        panelForm.add(new JLabel("Data Venda (YYYY-MM-DD):"));
        txtDataVenda = new JTextField();
        panelForm.add(txtDataVenda);

        panelForm.add(new JLabel("Cód. Produto:"));
        txtCodigoProduto = new JTextField();
        panelForm.add(txtCodigoProduto);

        panelForm.add(new JLabel("Quantidade:"));
        txtQuantidade = new JTextField();
        panelForm.add(txtQuantidade);

        panelForm.add(new JLabel("Modo Pagamento ($/X/D/C/T/F):"));
        txtModoPagamento = new JTextField();
        panelForm.add(txtModoPagamento);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSalvar = new JButton("Salvar");
        panelBotao.add(btnSalvar);
        add(panelBotao, BorderLayout.SOUTH);
    }

    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> salvarVenda());
    }

    private void preencherCamposParaEdicao() {
        lblCodigoValor.setText(String.valueOf(vendaParaEditar.getCodigo()));
        if (vendaParaEditar.getCodigoCliente() != null) {
            txtCodigoCliente.setText(String.valueOf(vendaParaEditar.getCodigoCliente()));
        }
        txtDataVenda.setText(vendaParaEditar.getDataVenda().toString());
        txtCodigoProduto.setText(String.valueOf(vendaParaEditar.getCodigoProduto()));
        txtQuantidade.setText(String.valueOf(vendaParaEditar.getQuantidade()));
        txtModoPagamento.setText(String.valueOf(vendaParaEditar.getModoPagamento().getCaracter()));
    }

    private void salvarVenda() {
        try {
            // Validações
            if (txtDataVenda.getText().trim().isEmpty() || txtCodigoProduto.getText().trim().isEmpty() || txtQuantidade.getText().trim().isEmpty() || txtModoPagamento.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Todos os campos obrigatórios devem ser preenchidos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate dataVenda = LocalDate.parse(txtDataVenda.getText());
            int codigoProduto = Integer.parseInt(txtCodigoProduto.getText());
            int quantidade = Integer.parseInt(txtQuantidade.getText());
            ModoPagamento modoPagamento = ModoPagamento.fromCaracter(txtModoPagamento.getText().charAt(0));

            Integer codigoCliente = null;
            if (!txtCodigoCliente.getText().trim().isEmpty()) {
                codigoCliente = Integer.parseInt(txtCodigoCliente.getText());
            }

            if (vendaParaEditar != null) {
                Venda vendaAtualizada = new Venda(vendaParaEditar.getCodigo(), codigoCliente, dataVenda, codigoProduto, quantidade, modoPagamento);
                vendaService.atualizar(vendaAtualizada);
                JOptionPane.showMessageDialog(this, "Venda atualizada com sucesso!");
            } else {
                Venda novaVenda = new Venda(0, codigoCliente, dataVenda, codigoProduto, quantidade, modoPagamento);
                vendaService.cadastrar(novaVenda);
                JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!");
            }

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            dispose();

        } catch (NumberFormatException | DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, verifique o formato dos campos numéricos ou da data.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Erro de Validação", JOptionPane.ERROR_MESSAGE);
        }
    }
}