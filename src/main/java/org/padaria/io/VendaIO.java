package org.padaria.io;

import org.padaria.model.ModoPagamento;
import org.padaria.model.Venda;
import org.padaria.util.CSVUtil;
import org.padaria.util.IOExceptionHandler;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendaIO implements ICSVReadable<Venda> {

    @Override
    public List<Venda> lerCSV(String caminhoArquivo) throws IOExceptionHandler {
        List<Venda> vendas = new ArrayList<>();
        List<String[]> linhas = CSVUtil.lerArquivoCSV(caminhoArquivo);
        for (String[] campos : linhas) {
            Venda venda = parsearLinhaCSV(campos);
            if (venda != null) {
                vendas.add(venda);
            }
        }
        return vendas;
    }

    @Override
    public void salvarCSV(List<Venda> lista, String caminhoArquivo) throws IOExceptionHandler {
        String[] cabecalho = { "codigo_cliente", "data_venda", "codigo_produto", "quantidade", "modo_pagamento" };
        List<String[]> dados = new ArrayList<>();

        for (Venda venda : lista) {
            dados.add(gerarLinhaCSV(venda));
        }
        CSVUtil.escreverArquivoCSV(caminhoArquivo, dados, cabecalho);
    }

    @Override
    public String[] gerarLinhaCSV(Venda entidade) {
        String codigoClienteStr = (entidade.getCodigoCliente() != null) ? String.valueOf(entidade.getCodigoCliente())
                : "";
        return new String[] {
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
        return new Venda(codigoCliente, dataVenda, codigoProduto, quantidade, modoPagamento);
    }
}