package com.rodrigo.consulta_endereco.service.implementation;

import com.rodrigo.consulta_endereco.model.entity.Endereco;
import com.rodrigo.consulta_endereco.model.repository.EnderecoRepository;
import com.rodrigo.consulta_endereco.service.EnderecoService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnderecoServiceImp implements EnderecoService {

    private EnderecoRepository enderecoRepository;

    @Override
    public Endereco salvarEndereco(Endereco endereco) {
        validar(endereco);
        return enderecoRepository.save(endereco);
    }

    @Override
    public Optional<Endereco> obterPorId(long id) {
        return enderecoRepository.findById(id);
    }

    @Override
    public void validar(Endereco endereco) {

    }
}
