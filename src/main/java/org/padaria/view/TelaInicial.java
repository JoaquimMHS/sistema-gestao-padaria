package org.padaria.view;

import javax.swing.*;
import java.awt.*;

public class TelaInicial extends JFrame {

    public TelaInicial() {
        setTitle("Sistema de Gestão - Padaria do Sr. Oak");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JLabel titulo = new JLabel("PADARIA DO SR. OAK", SwingConstants.CENTER);
        titulo.setFont(new Font("Arial", Font.BOLD, 18));

        JButton btnProdutos = new JButton("Gerenciar Produtos");
        JButton btnClientes = new JButton("Gerenciar Clientes");
        JButton btnFornecedores = new JButton("Gerenciar Fornecedores");
        JButton btnSair = new JButton("Sair");

        btnProdutos.addActionListener(e -> {
            // Ação para abrir a tela de produtos
            SwingUtilities.invokeLater(() -> {
                new TelaProduto().setVisible(true);
            });
        });        btnClientes.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir tela de Clientes"));
        btnFornecedores.addActionListener(e -> JOptionPane.showMessageDialog(this, "Abrir tela de Fornecedores"));
        btnSair.addActionListener(e -> System.exit(0));

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));

        panel.add(titulo);
        panel.add(btnProdutos);
        panel.add(btnClientes);
        panel.add(btnFornecedores);
        panel.add(btnSair);

        add(panel);
    }
}
