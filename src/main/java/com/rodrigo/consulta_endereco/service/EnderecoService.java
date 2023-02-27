package com.rodrigo.consulta_endereco.service;

import com.rodrigo.consulta_endereco.model.entity.Endereco;

import java.util.Optional;

public interface EnderecoService {

    Endereco salvarEndereco(Endereco endereco);

    Optional<Endereco> obterPorId(long id);

    void validar(Endereco endereco);
}
