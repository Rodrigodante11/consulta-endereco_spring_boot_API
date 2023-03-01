package com.rodrigo.consulta_endereco.utils;

import com.rodrigo.consulta_endereco.controller.dto.EnderecoDTO;

public class EnderecoCriacaoDTO {

    public static String CEP = "37548000";
    public static String RUA = "Gilberto da Silva Viana";
    public static String COMPLEMENTO = "Casa";
    public static String BAIRRO = "Chacara";
    public static String CIDADE = "Conceicao dos ouros";
    public static String ESTADO = "MG";

    public static EnderecoDTO criarEnderecoDTO(){
        return EnderecoDTO.builder()
                .cep(CEP)
                .rua(RUA)
                .complemento(COMPLEMENTO)
                .bairro(BAIRRO)
                .cidade(CIDADE)
                .estado(ESTADO)
                .build();
    }
}
