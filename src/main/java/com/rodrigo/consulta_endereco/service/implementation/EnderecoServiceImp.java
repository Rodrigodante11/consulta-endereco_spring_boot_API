package com.rodrigo.consulta_endereco.service.implementation;

import com.rodrigo.consulta_endereco.api.dto.CepAPIExternaDTO;
import com.rodrigo.consulta_endereco.exception.ErroCadastroEnderecoException;
import com.rodrigo.consulta_endereco.model.entity.Endereco;
import com.rodrigo.consulta_endereco.model.repository.EnderecoRepository;
import com.rodrigo.consulta_endereco.service.EnderecoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class EnderecoServiceImp implements EnderecoService {

    private final EnderecoRepository enderecoRepository;

    public EnderecoServiceImp(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Endereco> buscarEnderecoPorCep(String cep){
        validarCep(cep);

        return enderecoRepository.findByCep(cep);
    }

    @Override // consultando uma API externa de CEP
    public CepAPIExternaDTO consultaCepAPIExterna(CepAPIExternaDTO cepApiExterna){
        validarCep(cepApiExterna.getCep());
        return new RestTemplate().getForEntity("https://viacep.com.br/ws/"+cepApiExterna.getCep()+"/json/", CepAPIExternaDTO.class).getBody();
    }

    @Override
    @Transactional
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

        if(endereco.getCidade() == null){
            throw new ErroCadastroEnderecoException("Informe uma Cidade");
        }

        if(endereco.getEstado() == null){
            throw new ErroCadastroEnderecoException("Informe um Estado");
        }

        validarCep(endereco.getCep());
    }

    public void validarCep(String cep){
        if(cep == null){
            throw new ErroCadastroEnderecoException("Informe um CEP");
        }
        if(cep.contains("-") ){
            throw new ErroCadastroEnderecoException("Informe um CEP Valido sem (-)");
        }
        else if(cep.length() !=8 ){
            throw new ErroCadastroEnderecoException("Tamanho do CEP invalido");
        }
    }

    public String verFretePorEstado(String estado){

        List<String> sudeste = Arrays.asList("MG", "SP", "RJ", "ES");
        List<String> sul = Arrays.asList("PR", "RS", "SC");
        List<String> centroOeste = Arrays.asList("MT", "MS", "GO", "ES");
        List<String> norderte = Arrays.asList("MA", "PI", "BA", "CE", "PE", "RN", "PB", "AL", "SE");
        List<String> norte = Arrays.asList("AM", "RO", "AC", "PR", "PA", "AP", "TO");

        if(sudeste.contains(estado)) return "R$ 7,85";
        else if (centroOeste.contains(estado)) return  "(R$ 12,50";
        else if (norderte.contains(estado)) return  "R$ 15,98";
        else if (sul.contains(estado)) return  "R$ 17,30";
        else if (norte.contains(estado)) return  "R$ 20,83";
        return "";

    }
}
