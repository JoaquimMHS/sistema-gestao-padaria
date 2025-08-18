// src/main/java/org/padaria/view/TelaInicial.java
package org.padaria.view;

import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class TelaInicial extends JFrame {

    public TelaInicial() {
        setTitle("Sistema de Gestão - Padaria do Sr. Oak");
        setSize(400, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel("PADARIA DO SR. OAK", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnProdutos = new JButton("Gerenciar Produtos");
        JButton btnClientes = new JButton("Gerenciar Clientes");
        JButton btnCompras = new JButton("Gerenciar Compras");
        JButton btnVendas = new JButton("Gerenciar Vendas");
        JButton btnRelatorios = new JButton("Gerar Relatórios"); // Novo botão
        JButton btnSair = new JButton("Sair");

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
}