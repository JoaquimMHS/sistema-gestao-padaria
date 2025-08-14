package org.padaria.model;

public enum TipoCliente {
    PESSOA_FISICA("F", "Pessoa Física"),
    PESSOA_JURIDICA("J", "Pessoa Jurídica");

    private String codigo;
    private String descricao;

    private TipoCliente(String codigo, String descricao) {
        this.codigo = codigo;
        this.descricao = descricao;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoCliente fromCodigo(String codigo) {
        for (TipoCliente tipo : TipoCliente.values()) {
            if (tipo.getCodigo().equalsIgnoreCase(codigo)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Código de tipo de cliente inválido: " + codigo);
    }
}