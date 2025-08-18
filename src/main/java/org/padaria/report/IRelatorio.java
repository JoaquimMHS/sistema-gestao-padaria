// src/main/java/org/padaria/report/IRelatorio.java
package org.padaria.report;

import java.io.IOException;
import java.util.List;

public interface IRelatorio {
    void gerar(String nomeArquivo) throws IOException;
    List<String[]> processarDados();
    String getCabecalho(); // O tipo de retorno deve ser String
}