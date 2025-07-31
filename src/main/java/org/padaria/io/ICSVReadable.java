package org.padaria.io;

import org.padaria.util.IOExceptionHandler;

import java.util.List;

public interface ICSVReadable<T> {
    List<T> lerCSV(String caminhoArquivo) throws IOExceptionHandler;
    void salvarCSV(List<T> lista, String caminhoArquivo) throws IOExceptionHandler;
    String[] gerarLinhaCSV(T entidade);
    T parsearLinhaCSV(String[] campos);
}
