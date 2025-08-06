package org.padaria.oi;

public class ClienteIO implements ICSVReadable {


    public List<T> lerCSV(String caminhoArquivo) throws IOExceptionHandler {
        // METODO QUE VEIO DA INTERFACE, FALTA IMPLEMENTAR
    }

    public void salvarCSV(List<T> lista, String caminhoArquivo) throws IOExceptionHandler {
        // METODO QUE VEIO DA INTERFACE, FALTA IMPLEMENTAR
    }

    public String[] gerarLinhaCSV(T entidade) { 
        // METODO QUE VEIO DA INTERFACE, FALTA IMPLEMENTAR
    }

    public T parsearLinhaCSV(String[] campos) {
        // METODO QUE VEIO DA INTERFACE, FALTA IMPLEMENTAR
    }
}