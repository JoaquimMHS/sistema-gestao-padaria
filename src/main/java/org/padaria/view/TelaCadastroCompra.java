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
import org.padaria.model.Fornecedor;
import org.padaria.model.Produto;
import org.padaria.service.CompraService;
import org.padaria.service.FornecedorService;
import org.padaria.service.ProdutoService;

public class TelaCadastroCompra extends JDialog {

    private final CompraService compraService;
    private final FornecedorService fornecedorService;
    private final ProdutoService produtoService;
    private final Runnable onSaveCallback;

    private JTextField txtNumeroNF, txtCodigoFornecedor, txtDataCompra, txtCodigoProduto, txtQuantidade;
    private JButton btnSalvar;

    public TelaCadastroCompra(Frame owner, CompraService compraService,
            FornecedorService fornecedorService,
            ProdutoService produtoService,
            Runnable onSaveCallback) {
        super(owner, true);

        this.compraService = compraService;
        this.fornecedorService = fornecedorService;
        this.produtoService = produtoService;
        this.onSaveCallback = onSaveCallback;

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        adicionarListeners();

        setTitle("Registrar Nova Compra");
        txtDataCompra.setText(LocalDate.now().toString());
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

    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> salvarCompra());
    }

    private void salvarCompra() {
        try {
            // Validação de campos obrigatórios
            if (txtNumeroNF.getText().trim().isEmpty() ||
                    txtCodigoFornecedor.getText().trim().isEmpty() ||
                    txtDataCompra.getText().trim().isEmpty() ||
                    txtCodigoProduto.getText().trim().isEmpty() ||
                    txtQuantidade.getText().trim().isEmpty()) {

                JOptionPane.showMessageDialog(this,
                        "Todos os campos devem ser preenchidos.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Coleta e conversão dos dados
            int numeroNF = Integer.parseInt(txtNumeroNF.getText().trim());
            int codigoFornecedor = Integer.parseInt(txtCodigoFornecedor.getText().trim());
            LocalDate dataCompra = LocalDate.parse(txtDataCompra.getText().trim());
            int codigoProduto = Integer.parseInt(txtCodigoProduto.getText().trim());
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());

            // Validação de valores positivos
            if (numeroNF <= 0 || codigoFornecedor <= 0 || codigoProduto <= 0 || quantidade <= 0) {
                JOptionPane.showMessageDialog(this,
                        "Todos os valores numéricos devem ser positivos.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validação de existência do fornecedor
            Fornecedor fornecedor = fornecedorService.buscar(codigoFornecedor);
            if (fornecedor == null) {
                JOptionPane.showMessageDialog(this,
                        "Erro: Fornecedor com código " + codigoFornecedor + " não encontrado.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validação de existência do produto
            Produto produto = produtoService.buscar(codigoProduto);
            if (produto == null) {
                JOptionPane.showMessageDialog(this,
                        "Erro: Produto com código " + codigoProduto + " não encontrado.",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validação de número da NF duplicado
            Compra compraExistente = compraService.buscar(numeroNF);
            if (compraExistente != null) {
                JOptionPane.showMessageDialog(this,
                        "Erro: Já existe uma compra com o número de NF " + numeroNF + ".",
                        "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Criação e registro da compra
            Compra novaCompra = new Compra(numeroNF, codigoFornecedor, dataCompra, codigoProduto, quantidade);

            compraService.cadastrar(novaCompra);

            // Atualiza o estoque do produto (adiciona a quantidade comprada)
            produtoService.atualizarEstoque(codigoProduto, quantidade);

            try {
                produtoService.salvarDados("produtos.csv");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this,
                        "Erro ao salvar estoque do produto: " + ex.getMessage(),
                        "Erro ao Salvar Estoque", JOptionPane.ERROR_MESSAGE);
            }

            JOptionPane.showMessageDialog(this,
                    String.format("Compra registrada com sucesso!%n" +
                            "Fornecedor: %s%n" +
                            "Produto: %s%n" +
                            "Quantidade: %d%n" +
                            "Estoque atualizado com sucesso!",
                            fornecedor.getNome(), produto.getDescricao(), quantidade));

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this,
                    "Por favor, verifique se os campos numéricos contêm apenas números válidos.",
                    "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this,
                    "O formato da data deve ser YYYY-MM-DD (ex: 2025-08-19).",
                    "Erro de Formato de Data", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this,
                    "Ocorreu um erro inesperado: " + ex.getMessage(),
                    "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}