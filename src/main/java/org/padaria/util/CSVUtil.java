package org.padaria.util;

import java.io.*;
import java.util.*;

public class CSVUtil {
//Métodos para parsing de CSV  -  Le e Escreve o Arquivo CSV
//Tratamento de separadores (;)
//Conversão de tipos

    public static final String SEPARADOR = ";";

    public static List<String[]> lerArquivoCSV(String caminho){
        List<String[]> linhas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(caminho))) {
            String linha;
            reader.readLine(); // pula o cabeçalho
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha.split(SEPARADOR));
            }
        } catch (IOException e) {
            IOExceptionHandler.handle(e);
        }
        return linhas;
    }

    public static void escreverArquivoCSV(String caminho, List<String[]> dados, String[] cabecalho) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminho))) {
            writer.println(String.join(SEPARADOR, cabecalho));
            for (String[] linha : dados) {
                writer.println(String.join(SEPARADOR, linha));
            }
        } catch (IOException e) {
            IOExceptionHandler.handle(e);
        }
    }
}
