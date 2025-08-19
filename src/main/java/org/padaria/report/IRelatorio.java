package org.padaria.report;

import java.io.IOException;
import java.util.List;

public interface IRelatorio {
    void gerar(String nomeArquivo) throws IOException;

    List<String[]> processarDados();

    String[] getCabecalho();
}