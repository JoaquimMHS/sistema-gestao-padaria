// Salve em: org/padaria/view/TelaCadastroProduto.java
package org.padaria.view;

import org.padaria.model.Produto;
import org.padaria.service.ProdutoService;

import javax.swing.*;
import java.awt.*;

public class TelaCadastroProduto extends JDialog {

    private final ProdutoService produtoService;
    private final Runnable onSaveCallback;
    private final Produto produtoParaEditar;

    private JLabel lblCodigoValor;
    private JTextField txtDescricao, txtEstoqueAtual, txtEstoqueMinimo, txtValorCusto, txtPercentualLucro;
    private JButton btnSalvar;

    public TelaCadastroProduto(Frame owner, ProdutoService service, Runnable onSaveCallback, Produto produtoParaEditar) {
        super(owner, "Dados do Produto", true);

        this.produtoService = service;
        this.onSaveCallback = onSaveCallback;
        this.produtoParaEditar = produtoParaEditar;

        setSize(400, 350);
        setLocationRelativeTo(owner);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        adicionarListeners();

        if (produtoParaEditar != null) {
            setTitle("Editar Produto");
            preencherCamposParaEdicao();
        } else {
            setTitle("Cadastrar Novo Produto");
            
            int proximoCodigo = 1;
            if (!produtoService.listar().isEmpty()) {
                proximoCodigo = produtoService.listar().stream().mapToInt(Produto::getCodigo).max().getAsInt() + 1;
            }
            lblCodigoValor.setText(String.valueOf(proximoCodigo));
        }
    }

    private void inicializarComponentes() {
        JPanel panelForm = new JPanel(new GridLayout(6, 2, 5, 10));
        panelForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        panelForm.add(new JLabel("Código:"));
        lblCodigoValor = new JLabel();
        lblCodigoValor.setFont(new Font("Arial", Font.BOLD, 14));
        panelForm.add(lblCodigoValor);

        panelForm.add(new JLabel("Descrição:"));
        txtDescricao = new JTextField();
        panelForm.add(txtDescricao);

        panelForm.add(new JLabel("Estoque Atual:"));
        txtEstoqueAtual = new JTextField();
        panelForm.add(txtEstoqueAtual);

        panelForm.add(new JLabel("Estoque Mínimo:"));
        txtEstoqueMinimo = new JTextField();
        panelForm.add(txtEstoqueMinimo);

        panelForm.add(new JLabel("Valor de Custo (R$):"));
        txtValorCusto = new JTextField();
        panelForm.add(txtValorCusto);

        panelForm.add(new JLabel("Percentual de Lucro (%):"));
        txtPercentualLucro = new JTextField();
        panelForm.add(txtPercentualLucro);

        add(panelForm, BorderLayout.CENTER);

        JPanel panelBotao = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btnSalvar = new JButton("Salvar");
        panelBotao.add(btnSalvar);
        add(panelBotao, BorderLayout.SOUTH);
    }

    private void adicionarListeners() {
        btnSalvar.addActionListener(e -> salvarProduto());
    }

    private void preencherCamposParaEdicao() {
        lblCodigoValor.setText(String.valueOf(produtoParaEditar.getCodigo()));
        txtDescricao.setText(produtoParaEditar.getDescricao());
        txtEstoqueAtual.setText(String.valueOf(produtoParaEditar.getEstoqueAtual()));
        txtEstoqueMinimo.setText(String.valueOf(produtoParaEditar.getEstoqueMinimo()));
        txtValorCusto.setText(String.valueOf(produtoParaEditar.getValorCusto()));
        txtPercentualLucro.setText(String.valueOf(produtoParaEditar.getPercentualLucro()));
    }

    private void salvarProduto() {
        if (txtDescricao.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "O campo 'Descrição' é obrigatório.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String descricao = txtDescricao.getText();
            int estoqueAtual = Integer.parseInt(txtEstoqueAtual.getText());
            int estoqueMinimo = Integer.parseInt(txtEstoqueMinimo.getText());
            double valorCusto = Double.parseDouble(txtValorCusto.getText().replace(",", "."));
            int percentualLucro = Integer.parseInt(txtPercentualLucro.getText());

            if (estoqueAtual < 0 || estoqueMinimo < 0 || valorCusto < 0 || percentualLucro < 0) {
                JOptionPane.showMessageDialog(this, "Valores de estoque, custo e lucro não podem ser negativos.", "Erro de Validação", JOptionPane.ERROR_MESSAGE);
                return;
            }

            if (produtoParaEditar != null) {
                Produto produtoAtualizado = new Produto(produtoParaEditar.getCodigo(), descricao, estoqueMinimo, estoqueAtual, valorCusto, percentualLucro);
                produtoService.atualizar(produtoAtualizado);
                JOptionPane.showMessageDialog(this, "Produto atualizado com sucesso!");
            } else {
                int novoCodigo = 1;
                if (!produtoService.listar().isEmpty()) {
                    novoCodigo = produtoService.listar().stream().mapToInt(Produto::getCodigo).max().getAsInt() + 1;
                }
                Produto novoProduto = new Produto(novoCodigo, descricao, estoqueMinimo, estoqueAtual, valorCusto, percentualLucro);
                produtoService.cadastrar(novoProduto);
                JOptionPane.showMessageDialog(this, "Produto cadastrado com sucesso!");
            }

            if (onSaveCallback != null) {
                onSaveCallback.run();
            }

            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Por favor, verifique se os campos numéricos foram preenchidos corretamente.", "Erro de Formato", JOptionPane.ERROR_MESSAGE);
        }
    }
}