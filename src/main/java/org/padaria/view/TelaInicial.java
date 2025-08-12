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
            TelaProduto telaProdutos = new TelaProduto(this);
            telaProdutos.setVisible(true);
            
            this.setVisible(false); 
        });

        btnClientes.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidade não implementada."));
        btnFornecedores.addActionListener(e -> JOptionPane.showMessageDialog(this, "Funcionalidade não implementada."));
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
