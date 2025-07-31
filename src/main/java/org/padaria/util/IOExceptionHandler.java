package org.padaria.util;

public class IOExceptionHandler extends RuntimeException{
    // Tratamento padronizado de erros I/O
    // Método para imprimir "Erro de I/O." e encerrar

    public static void handle(Exception e){
        System.out.println("Erro de I/O.");
        System.exit(1);
    }
}
