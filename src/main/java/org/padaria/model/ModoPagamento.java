package org.padaria.model;

public enum ModoPagamento {
    DINHEIRO('$'),
    CHEQUE('X'),
    DEBITO('D'),
    CREDITO('C'),
    TICKET('T'),
    FIADO('F');

    private final char caracter;

    ModoPagamento(char caracter) {
        this.caracter = caracter;
    }

    public char getCaracter() {
        return caracter;
    }

    public static ModoPagamento fromCaracter(char caracter) {
        for (ModoPagamento modo : ModoPagamento.values()) {
            if (modo.getCaracter() == caracter) {
                return modo;
            }
        }
        throw new IllegalArgumentException("Caractere de modo de pagamento inválido: " + caracter);
    }
}