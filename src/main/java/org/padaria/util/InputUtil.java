package org.padaria.util;

import java.util.Scanner;

public class InputUtil {
    // Métodos para leitura segura do teclado
    // Validação de entrada

    private static final Scanner sc = new Scanner(System.in);

    public static int lerInt(String mensagem) {
        System.out.print(mensagem + ": ");
        while (!sc.hasNextInt()) {
            System.out.print("Valor inválido. Digite um número inteiro: ");
            sc.next();
        }
        return sc.nextInt();
    }

    public static double lerDouble(String mensagem) {
        System.out.print(mensagem + ": ");
        while (!sc.hasNextDouble()) {
            System.out.print("Valor inválido. Digite um número decimal: ");
            sc.next();
        }
        return sc.nextDouble();
    }

    public static String lerString(String mensagem) {
        System.out.print(mensagem + ": ");
        sc.nextLine();
        return sc.nextLine().trim();
    }
}
