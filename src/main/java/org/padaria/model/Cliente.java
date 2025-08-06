package org.padaria.model;

import java.time.LocalDateTime;

public class Cliente implements IEntity{
    private int codigoIdentificador;
    private String nome;
    private String Endereco;
    private String Telefone;
    private LocalDateTime data;

    int getCodigo(){
        return codigoIdentificador;
        // METODO QUE VEIO DA INTERFACE, CONFIMAR SE É SÓ ISSO
    }

    void setCodigo(int codigo){
        this.codigoIdentificador = codigo;
        // METODO QUE VEIO DA INTERFACE, CONFIMAR SE É SÓ ISSO
    }

    boolean isValid(){
        // METODO QUE VEIO DA INTERFACE, FALTA IMPLEMENTAR
    }

}