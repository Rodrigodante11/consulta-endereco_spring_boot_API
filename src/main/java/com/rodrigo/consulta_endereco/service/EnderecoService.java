package com.rodrigo.consulta_endereco.service;

import com.rodrigo.consulta_endereco.api.dto.CepAPIExternaDTO;
import com.rodrigo.consulta_endereco.model.entity.Endereco;

import java.util.Optional;

public interface EnderecoService {

    Endereco salvarEndereco(Endereco endereco);

    Endereco atualizarEndereco(Endereco endereco);

    void deletarEndereco(Endereco endereco);

    Optional<Endereco> buscarEnderecoPorCep(String cep);

    Optional<Endereco> obterPorId(long id);

    void validar(Endereco endereco);

    // consultando uma API externa de CEP
    CepAPIExternaDTO consultaCepAPIExterna(CepAPIExternaDTO cepApiExterna);

    String verFretePorEstado(String estado);

}
