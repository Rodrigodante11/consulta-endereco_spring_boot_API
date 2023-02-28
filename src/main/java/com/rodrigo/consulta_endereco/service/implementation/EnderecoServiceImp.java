package com.rodrigo.consulta_endereco.service.implementation;

import com.rodrigo.consulta_endereco.exception.ErroCadastroEndereco;
import com.rodrigo.consulta_endereco.model.entity.Endereco;
import com.rodrigo.consulta_endereco.model.repository.EnderecoRepository;
import com.rodrigo.consulta_endereco.service.EnderecoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
public class EnderecoServiceImp implements EnderecoService {

    private EnderecoRepository enderecoRepository;

    @Override
    public Optional<Endereco> buscarEnderecoPorCep(String cep){

        return enderecoRepository.findByCep(cep);
    }

    @Override
    public Endereco salvarEndereco(Endereco endereco) {
        validar(endereco);
        return enderecoRepository.save(endereco);
    }

    @Override
    @Transactional
    public Endereco atualizarEndereco(Endereco endereco){
        Objects.requireNonNull(endereco.getId());
        validar(endereco);
        return enderecoRepository.save(endereco);
    }

    @Override
    @Transactional
    public void deletarEndereco(Endereco endereco){
        Objects.requireNonNull(endereco.getId());
        enderecoRepository.delete(endereco);
    }



    @Override
    public Optional<Endereco> obterPorId(long id) {
        return enderecoRepository.findById(id);
    }

    @Override
    public void validar(Endereco endereco) {
        if(endereco.getCep() == null){
            throw new ErroCadastroEndereco(" Informe um CEP");
        }

        if(endereco.getCep().length() !=7 ){
            throw new ErroCadastroEndereco(" Tamanho do CEP invalido ");
        }

        if(endereco.getCidade() == null){
            throw new ErroCadastroEndereco(" Informe uma Cidade");
        }

        if(endereco.getEstado() == null){
            throw new ErroCadastroEndereco(" Informe um Estado");
        }

        if(endereco.getRegiao() == null){
            throw new ErroCadastroEndereco(" Informe uma Regiao");
        }

    }
}
