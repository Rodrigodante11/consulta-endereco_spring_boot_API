package com.rodrigo.consulta_endereco.api.resources;

import com.rodrigo.consulta_endereco.api.dto.CepAPIExternaDTO;
import com.rodrigo.consulta_endereco.api.dto.EnderecoDTO;
import com.rodrigo.consulta_endereco.exception.ErroCadastroEnderecoException;
import com.rodrigo.consulta_endereco.model.entity.Endereco;
import com.rodrigo.consulta_endereco.model.enums.Regiao;
import com.rodrigo.consulta_endereco.service.EnderecoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
public class EnderecoResouces {

    private final EnderecoService enderecoService;

    //Consustando a API EXterna do VIACEP
    @GetMapping(value = "/consulta-endereco-api-enterna")
    public ResponseEntity consultaCepAPIExterna(@RequestBody CepAPIExternaDTO cep){
        try{
            CepAPIExternaDTO cepAPIExternaDTO = enderecoService.consultaCepAPIExterna(cep);

            cepAPIExternaDTO.setFrete(enderecoService.verFretePorEstado(cepAPIExternaDTO.getUf()));
            return  new ResponseEntity<>(cepAPIExternaDTO, HttpStatus.OK);
        }
        catch (ErroCadastroEnderecoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //CONSULTANDO A API INTERNA DA BASE DE DADOS
    @GetMapping(value = "/consulta-endereco")
    public ResponseEntity consultaCepAPIInterna(@RequestBody EnderecoDTO enderecoDTO){

        Endereco endereco =  new Endereco();
        endereco.setCep(enderecoDTO.getCep());

        Optional<Endereco> enderecoEncontrado = enderecoService.buscarEnderecoPorCep(endereco.getCep());

        if(!enderecoEncontrado.isPresent()){
            return ResponseEntity.badRequest().body(
                    "Nao foi possivel realizar a consulta, Endereco nao enocontrado para o CEP informado");
        }
        return ResponseEntity.ok(enderecoEncontrado);
    }

    @PostMapping(value = "/criar-endereco")
    public ResponseEntity salvarEndereco(@RequestBody EnderecoDTO enderecoDTO){

        Endereco endereco = converterDTO(enderecoDTO);

        try{
            Endereco enderecosalvo = enderecoService.salvarEndereco(endereco);
            return new ResponseEntity(enderecosalvo, HttpStatus.CREATED);

        }catch (ErroCadastroEnderecoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/atualizar/{id}")
    public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody EnderecoDTO enderecoDTO){

        return enderecoService.obterPorId(id).map( entity -> {
            try {
                Endereco endereco = converterDTO(enderecoDTO);
                endereco.setId(entity.getId());
                enderecoService.atualizarEndereco(endereco);
                return ResponseEntity.ok(endereco);
            }catch (ErroCadastroEnderecoException e) {
                return ResponseEntity.badRequest().body(e.getMessage());
            }
        }).orElseGet( () ->
                new ResponseEntity("Endereco não encontrado na base de Dados.", HttpStatus.BAD_REQUEST) );
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity deletar(@PathVariable("id") Long id ){

        return enderecoService.obterPorId(id).map( entidade -> {
            enderecoService.deletarEndereco(entidade);
            return new ResponseEntity(HttpStatus.NO_CONTENT);

        }).orElseGet( () ->
                new ResponseEntity("Endereco nao encontrado na base de dados", HttpStatus.BAD_REQUEST)
        );
    }


    private Endereco converterDTO(EnderecoDTO enderecoDTO){
        return Endereco.builder()
                .cep(enderecoDTO.getCep())
                .rua(enderecoDTO.getRua())
                .complemento(enderecoDTO.getComplemento())
                .bairro(enderecoDTO.getBairro())
                .cidade(enderecoDTO.getCidade())
                .estado(enderecoDTO.getEstado()).build();
    }
}
