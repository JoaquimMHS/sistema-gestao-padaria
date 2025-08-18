package org.padaria.util;

import java.util.Scanner;
import java.util.List;

public class MenuUtil {
    // Métodos auxiliares para exibição de menus

    private static final Scanner sc = new Scanner(System.in);

    public static int exibirMenu(String titulo, List<String> opcoes) {
        System.out.println("\n=== " + titulo + " ===");
        for (int i = 0; i < opcoes.size(); i++) {
            System.out.printf("%d - %s\n", i + 1, opcoes.get(i));
        }
        System.out.print("Escolha uma opção: ");

        while (!sc.hasNextInt()) {
            System.out.print("Opção inválida. Tente novamente: ");
            sc.next();
        }
        return sc.nextInt();
    }

    public static int lerOpcao(Scanner scanner) {
        return 0;
    }
}
