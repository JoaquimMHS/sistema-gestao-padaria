// src/main/java/org/padaria/io/VendaIO.java
package org.padaria.io;

import org.padaria.model.ModoPagamento;
import org.padaria.model.Venda;
import org.padaria.util.IOExceptionHandler;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendaIO implements ICSVReadable<Venda> {

    @Override
    public List<Venda> lerCSV(String caminhoArquivo) throws IOExceptionHandler {
        List<Venda> vendas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminhoArquivo))) {
            br.readLine(); // Pula o cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                vendas.add(parsearLinhaCSV(linha.split(";")));
            }
        } catch (IOException e) {
            IOExceptionHandler.handle("Erro ao ler o arquivo CSV de vendas.", e);
        }
        return vendas;
    }

    @Override
    public void salvarCSV(List<Venda> lista, String caminhoArquivo) throws IOExceptionHandler {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            bw.write("codigo_cliente;data_venda;codigo_produto;quantidade;modo_pagamento");
            bw.newLine();
            for (Venda venda : lista) {
                String[] campos = gerarLinhaCSV(venda);
                String linha = String.join(";", campos);
                bw.write(linha);
                bw.newLine();
            }
        } catch (IOException e) {
            IOExceptionHandler.handle("Erro ao salvar o arquivo CSV de vendas.", e);
        }
    }

    @Override
    public String[] gerarLinhaCSV(Venda entidade) {
        String codigoClienteStr = (entidade.getCodigoCliente() != null) ? String.valueOf(entidade.getCodigoCliente()) : "";
        return new String[]{
                codigoClienteStr,
                entidade.getDataVenda().toString(),
                String.valueOf(entidade.getCodigoProduto()),
                String.valueOf(entidade.getQuantidade()),
                String.valueOf(entidade.getModoPagamento().getCaracter())
        };
    }

    @Override
    public Venda parsearLinhaCSV(String[] campos) {
        Integer codigoCliente = null;
        if (!campos[0].isEmpty()) {
            codigoCliente = Integer.parseInt(campos[0]);
        }
        LocalDate dataVenda = LocalDate.parse(campos[1]);
        int codigoProduto = Integer.parseInt(campos[2]);
        int quantidade = Integer.parseInt(campos[3]);
        ModoPagamento modoPagamento = ModoPagamento.fromCaracter(campos[4].charAt(0));
        return new Venda(0, codigoCliente, dataVenda, codigoProduto, quantidade, modoPagamento);
    }
}