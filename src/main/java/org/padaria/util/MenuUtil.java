package org.padaria.util;

import java.util.Scanner;
import java.util.InputMismatchException;
import java.util.List;

public class MenuUtil {

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
        while (true) {
            try {
                int opcao = scanner.nextInt();
                scanner.nextLine();
                return opcao;
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Digite um número: ");
                scanner.nextLine();
            }
        }
    }

    public static double lerDouble(Scanner scanner) {
        while (true) {
            try {
                double valor = scanner.nextDouble();
                scanner.nextLine();
                return valor;
            } catch (InputMismatchException e) {
                System.out.print("Entrada inválida. Digite um número decimal: ");
                scanner.nextLine();
            }
        }
    }
}
