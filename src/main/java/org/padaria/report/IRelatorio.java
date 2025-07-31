package org.padaria.report;

import java.util.List;

public interface IRelatorio {
    void gerar(String nomeArquivo); // gera o arquivo CSV
    List<String[]> processarDados();
    String[] getCabecalho();
}
