package org.padaria.view;

import org.padaria.model.Cliente;
import org.padaria.model.ModoPagamento;
import org.padaria.model.Produto;
import org.padaria.model.Venda;
import org.padaria.service.ClienteService;
import org.padaria.service.ProdutoService;
import org.padaria.service.VendaService;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class TelaCadastroVenda extends JDialog {

    private final VendaService vendaService;
    private final ClienteService clienteService;
    private final ProdutoService produtoService;
    private final Runnable onSaveCallback;

    private JLabel lblCodigoValor;
    private JTextField txtCodigoCliente, txtDataVenda, txtCodigoProduto, txtQuantidade, txtModoPagamento;
    private JButton btnSalvar;

    public TelaCadastroVenda(Frame owner, VendaService vService, ClienteService cService, ProdutoService pService, Runnable onSaveCallback) {
        super(owner, "Registrar Nova Venda", true);

        this.vendaService = vService;
        this.clienteService = cService;
        this.produtoService = pService;
        this.onSaveCallback = onSaveCallback;

        setSize(400, 300);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        adicionarListeners();

        lblCodigoValor.setText("(Automático)");
    }

    private void inicializarComponentes() {
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Código Venda:"));
        lblCodigoValor = new JLabel();
        lblCodigoValor.setFont(new Font("Arial", Font.BOLD, 14));
        panelForm.add(lblCodigoValor);

        panelForm.add(new JLabel("Cód. Cliente (obrigatório se Fiado):"));
        txtCodigoCliente = new JTextField();
        panelForm.add(txtCodigoCliente);

        panelForm.add(new JLabel("Data Venda (YYYY-MM-DD):"));
        txtDataVenda = new JTextField(LocalDate.now().toString());
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

    private void salvarVenda() {
        try {
            // Validação de campos obrigatórios
            if (txtDataVenda.getText().trim().isEmpty() || txtCodigoProduto.getText().trim().isEmpty() || txtQuantidade.getText().trim().isEmpty() || txtModoPagamento.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Os campos Data, Produto, Quantidade e Pagamento são obrigatórios.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Coleta de dados e conversão inicial
            int codigoProduto = Integer.parseInt(txtCodigoProduto.getText().trim());
            int quantidade = Integer.parseInt(txtQuantidade.getText().trim());
            ModoPagamento modoPagamento = ModoPagamento.fromCaracter(txtModoPagamento.getText().trim().toUpperCase().charAt(0));

            // Validação de existência do produto
            Produto produto = produtoService.buscar(codigoProduto);
            if (produto == null) {
                JOptionPane.showMessageDialog(this, "Erro: Produto com código " + codigoProduto + " não encontrado.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Validação de existência do cliente (se um código for inserido)
            Integer codigoCliente = null;
            if (!txtCodigoCliente.getText().trim().isEmpty()) {
                codigoCliente = Integer.parseInt(txtCodigoCliente.getText().trim());
                Cliente cliente = clienteService.buscar(codigoCliente);
                if (cliente == null) {
                    JOptionPane.showMessageDialog(this, "Erro: Cliente com código " + codigoCliente + " não encontrado.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }

            // Validação da regra de negócio para pagamento fiado
            if (modoPagamento == ModoPagamento.FIADO && codigoCliente == null) {
                JOptionPane.showMessageDialog(this, "Para vendas no modo 'Fiado', o Código do Cliente é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            LocalDate dataVenda = LocalDate.parse(txtDataVenda.getText().trim());

            // Criação e registro da venda
            Venda novaVenda = new Venda(codigoCliente, dataVenda, codigoProduto, quantidade, modoPagamento);
            vendaService.cadastrar(novaVenda);
            JOptionPane.showMessageDialog(this, "Venda registrada com sucesso!");

            if (onSaveCallback != null) {
                onSaveCallback.run(); // Atualiza a tabela da tela anterior
            }

            dispose(); // Fecha a janela de cadastro

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, verifique o formato dos campos numéricos.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException ex) {
            JOptionPane.showMessageDialog(this, "O formato da data é inválido. Use AAAA-MM-DD.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Ocorreu um erro: " + ex.getMessage(), "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }
}