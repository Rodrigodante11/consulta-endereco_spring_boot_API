package com.rodrigo.consulta_endereco.controller.resources;

import com.rodrigo.consulta_endereco.controller.dto.CepAPIExternaDTO;
import com.rodrigo.consulta_endereco.controller.dto.EnderecoDTO;
import com.rodrigo.consulta_endereco.exception.ErroCadastroEnderecoException;
import com.rodrigo.consulta_endereco.model.entity.Endereco;
import com.rodrigo.consulta_endereco.service.EnderecoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("v1")
@RequiredArgsConstructor
@Api(value = "API REST Endereco")
@CrossOrigin(origins = "*")
public class EnderecoResources {

    private final EnderecoService enderecoService;

    //CONSULTANDO a API EXTERNA DO VIACEP
    //CONSULTANDO a API EXTERNA DO VIACEP
    @PostMapping(value = "/consulta-endereco-api-externa")
    @ApiOperation(value = "Consulta o Endereco por CEP informado (API Externa)")
    public ResponseEntity consultaCepAPIExterna(@RequestBody(required = false) Map cep){
        try{
            CepAPIExternaDTO cepAPIExternaDTO = CepAPIExternaDTO.builder().cep(cep.get("cep").toString()).build();

            cepAPIExternaDTO = enderecoService.consultaCepAPIExterna(cepAPIExternaDTO);

            cepAPIExternaDTO.setFrete(enderecoService.verFretePorEstado(cepAPIExternaDTO.getUf()));
            return  new ResponseEntity<>(cepAPIExternaDTO, HttpStatus.OK);
        }
        catch (ErroCadastroEnderecoException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //CONSULTANDO A API INTERNA DA BASE DE DADOS
    @PostMapping(value = "/consulta-endereco")
    @ApiOperation(value = "Consulta o Endereco por CEP informado (API Interna)")
    public ResponseEntity consultaCepAPIInterna( @RequestBody(required = false) Map cep) {
        try {

            Endereco endereco = new Endereco();
            endereco.setCep(cep.get("cep").toString());

            Optional<Endereco> enderecoEncontrado = enderecoService.buscarEnderecoPorCep(endereco.getCep());

            if (!enderecoEncontrado.isPresent()) {
                return ResponseEntity.badRequest().body(
                        "Nao foi possivel realizar a consulta, Endereco nao encontrado para o CEP informado");
            }

            EnderecoDTO enderecoDTORequest = converter(enderecoEncontrado.get());
            enderecoDTORequest.setFrete(enderecoService.verFretePorEstado(enderecoDTORequest.getEstado()));

            return ResponseEntity.ok(enderecoDTORequest);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/criar-endereco")
    @ApiOperation(value = "Cria um Endereco  (API Interna)")
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
    @ApiOperation(value = "Atualiza um Endereco  (API Interna)")
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
    @ApiOperation(value = "Deleta um Endereco  (API Interna)")
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
    private EnderecoDTO converter(Endereco endereco){
        return EnderecoDTO.builder()
                .id(endereco.getId())
                .cep(endereco.getCep())
                .rua(endereco.getRua())
                .complemento(endereco.getComplemento())
                .bairro(endereco.getBairro())
                .cidade(endereco.getCidade())
                .estado(endereco.getEstado()).build();
    }
}
