package org.padaria;

import org.padaria.view.TelaInicial;

import javax.swing.*;

public class App {
    /**
     * Essa classe inicia o programa com interface gráfica
     * 
     * @param args
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            TelaInicial tela = new TelaInicial();
            tela.setVisible(true);
        });
    }
}
