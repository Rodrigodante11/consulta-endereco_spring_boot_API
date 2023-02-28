package com.rodrigo.consulta_endereco.utils;

import com.rodrigo.consulta_endereco.model.entity.Endereco;

public class EnderecoCriacao { // usando para os testes nao precisar usar varias criacoes em cada classe de teste

    public static String CEP = "37548000";
    public static String RUA = "Gilberto da Silva Viana";
    public static String COMPLEMENTO = "Casa";
    public static String BAIRRO = "Chacara";
    public static String CIDADE = "Conceicao dos ouros";
    public static String ESTADO = "MG";

    public static Endereco criarEndereco(){
        return Endereco.builder()
                .cep(CEP)
                .rua(RUA)
                .complemento(COMPLEMENTO)
                .bairro(BAIRRO)
                .cidade(CIDADE)
                .estado(ESTADO)
                .build();
    }
}
