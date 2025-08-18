// src/main/java/org/padaria/view/TelaInicial.java
package org.padaria.view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.*;

public class TelaInicial extends JFrame {

    private CardLayout cardLayout;
    private JPanel contentPanel;

    public TelaInicial() {
        setTitle("Sistema Padaria");
        setSize(1000, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        JLabel titulo = new JLabel("Sistema Padaria", SwingConstants.CENTER);
        titulo.setFont(new Font("Comic Sans MS", Font.BOLD, 26));
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titulo, BorderLayout.NORTH);

        JPanel menuPanel = new JPanel(new GridLayout(7, 1, 10, 10));
        menuPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        menuPanel.setPreferredSize(new Dimension(180, 0));


        JButton btnClientes = new JButton("Clientes");
        JButton btnFornecedores = new JButton("Fornecedores");
        JButton btnProdutos = new JButton("Produtos");
        JButton btnVendas = new JButton("Vendas");
        JButton btnCompras = new JButton("Compras");
        JButton btnRelatorios = new JButton("Relatórios");
        JButton btnSair = new JButton("Sair");

        menuPanel.add(btnClientes);
        menuPanel.add(btnFornecedores);
        menuPanel.add(btnProdutos);
        menuPanel.add(btnVendas);
        menuPanel.add(btnCompras);
        menuPanel.add(btnRelatorios);
        menuPanel.add(btnSair);

        add(menuPanel, BorderLayout.WEST);

        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);

        JLabel mensagem = new JLabel("<html><div style='text-align: center;'>Bem vindo ao Sistema<br/>feito por Eliaquim, Felicio, Joaquim, João Pedro e Nicolas</div></html>", SwingConstants.CENTER);
        mensagem.setFont(new Font("Comic Sans MS", Font.PLAIN, 20));
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.add(mensagem, BorderLayout.CENTER);

        add(contentPanel, BorderLayout.CENTER);

        btnClientes.addActionListener(e -> {
            TelaCliente telaClientes = new TelaCliente();
            telaClientes.setVisible(true);
        });
        btnFornecedores.addActionListener(e -> {
            TelaFornecedor telaFornecedor = new TelaFornecedor(this);
            telaFornecedor.setVisible(true);
            this.setVisible(false);
        });
        btnCompras.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidade não implementada."));
        btnProdutos.addActionListener(e -> {
            TelaProduto telaProdutos = new TelaProduto(this);
            telaProdutos.setVisible(true);
            this.setVisible(false);
        });

        btnClientes.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidade não implementada."));

        btnCompras.addActionListener(e -> {
            TelaCompras telaCompras = new TelaCompras(this);
            telaCompras.setVisible(true);
            this.setVisible(false);
        });

        btnVendas.addActionListener(e -> {
            TelaVendas telaVendas = new TelaVendas(this);
            telaVendas.setVisible(true);
            this.setVisible(false);
        });

        // Conectando o novo botão à tela de relatórios
        btnRelatorios.addActionListener(e -> {
            TelaRelatorios telaRelatorios = new TelaRelatorios(this);
            telaRelatorios.setVisible(true);
            this.setVisible(false);
        });

        btnSair.addActionListener(e -> System.exit(0));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10)); // Ajustado para 7 linhas
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        panel.add(titulo);
        panel.add(btnProdutos);
        panel.add(btnClientes);
        panel.add(btnCompras);
        panel.add(btnVendas);
        panel.add(btnRelatorios); // Adicionando o novo botão ao painel
        panel.add(btnSair);

        add(panel);
    }

            this.setVisible(false);
        });
        btnSair.addActionListener(e -> System.exit(0));
    }

    public static void main(String[] args) {
        new TelaInicial().setVisible(true);
    }
}