package com.rodrigo.consulta_endereco.service;

import com.rodrigo.consulta_endereco.exception.ErroCadastroEnderecoException;
import com.rodrigo.consulta_endereco.model.entity.Endereco;
import com.rodrigo.consulta_endereco.model.repository.EnderecoRepository;
import com.rodrigo.consulta_endereco.service.implementation.EnderecoServiceImp;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.rodrigo.consulta_endereco.utils.EnderecoCriacao;

import java.util.Optional;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test")
public class EnderecoServiceTest {

    @SpyBean
    EnderecoServiceImp enderecoServiceImp;
    @MockBean
    EnderecoRepository enderecoRepository;

    @Test
    public void deveSalvarUmEndereco(){
        Endereco enderecoSalvar = EnderecoCriacao.criarEndereco();

        Assertions.assertDoesNotThrow(() ->{

            Mockito.doNothing().when(enderecoServiceImp).validar(enderecoSalvar); // nao quero testar o validar agora/ Test unitario

            Endereco enderecoSalvo = EnderecoCriacao.criarEndereco();

            System.out.println(enderecoSalvo);
            Mockito.when(enderecoRepository.save(enderecoSalvar)).thenReturn(enderecoSalvo); // classe que nao quero testar
            //Mockito.doReturn(enderecoSalvo).when(enderecoRepository).save(enderecoSalvar);
            Endereco enderecoSalvoImp =  enderecoServiceImp.salvarEndereco(enderecoSalvar);

            Assertions.assertNotNull(enderecoSalvoImp);
            Assertions.assertEquals(enderecoSalvoImp.getId(), enderecoSalvo.getId());
            Assertions.assertEquals(enderecoSalvoImp.getCep(), enderecoSalvo.getCep());
            Assertions.assertEquals(enderecoSalvoImp.getRua(), enderecoSalvo.getRua());
            Assertions.assertEquals(enderecoSalvoImp.getComplemento(), enderecoSalvo.getComplemento());
            Assertions.assertEquals(enderecoSalvoImp.getBairro(), enderecoSalvo.getBairro());
            Assertions.assertEquals(enderecoSalvoImp.getCidade(), enderecoSalvo.getCidade());
            Assertions.assertEquals(enderecoSalvoImp.getEstado(), enderecoSalvo.getEstado());

        });
    }

    @Test
    public void deveAtualizarUmEndereco(){
        Endereco enderecoSalvo = EnderecoCriacao.criarEndereco();
        enderecoSalvo.setId(1L);

        Mockito.doNothing().when(enderecoServiceImp).validar(enderecoSalvo);

        enderecoServiceImp.atualizarEndereco(enderecoSalvo);
    }

    @Test
    public void deveLancarErroAoAtualizarUmEnderecoQueNaoFoiSalvo(){
        Assertions.assertThrows(NullPointerException.class, () -> {
            Endereco enderecoSalvo = EnderecoCriacao.criarEndereco();

            Mockito.doNothing().when(enderecoServiceImp).validar(enderecoSalvo);

            enderecoServiceImp.atualizarEndereco(enderecoSalvo);

        });
    }

    @Test
    public void deveDeletarUmEndereco(){

        Endereco endereco = EnderecoCriacao.criarEndereco();
        endereco.setId(1L);

        enderecoServiceImp.deletarEndereco(endereco);

        Mockito.verify(enderecoRepository).delete(endereco);

    }

    @Test
    public void deveLancarErroAoTentarDelearUmEnderecoNaoSalvo(){
        Assertions.assertThrows(NullPointerException.class, () -> {
            Endereco endereco = EnderecoCriacao.criarEndereco();

            enderecoServiceImp.deletarEndereco(endereco);

            Mockito.verify(enderecoRepository, Mockito.never()).delete(endereco);
        });
    }

    @Test
    public void deveBuscarUmEnderecoPorCep(){
        Endereco endereco = EnderecoCriacao.criarEndereco();

        Assertions.assertDoesNotThrow(() -> {

            endereco.setId(1L);

            Mockito.when(enderecoRepository.findByCep(EnderecoCriacao.CEP)).thenReturn(Optional.of(endereco));

            enderecoServiceImp.buscarEnderecoPorCep(EnderecoCriacao.CEP);

        });

        Assertions.assertEquals(endereco.getId(), 1L);
        Assertions.assertEquals(endereco.getCep(), EnderecoCriacao.CEP);
        Assertions.assertEquals(endereco.getRua(), EnderecoCriacao.RUA);
        Assertions.assertEquals(endereco.getComplemento(), EnderecoCriacao.COMPLEMENTO);
        Assertions.assertEquals(endereco.getBairro(), EnderecoCriacao.BAIRRO);
        Assertions.assertEquals(endereco.getCidade(), EnderecoCriacao.CIDADE);
        Assertions.assertEquals(endereco.getEstado(), EnderecoCriacao.ESTADO);

    }

    @Test
    public void deveRetornaValidoAoconsultarCEPValido(){
        Assertions.assertDoesNotThrow(() ->{

            Endereco endereco = EnderecoCriacao.criarEndereco();

            Mockito.doNothing().when(enderecoServiceImp).validarCep(endereco.getCep());

            enderecoServiceImp.validar(endereco);
        });
    }

    @Test
    public void ErroDeValidaoPorCidadeVazio() {

        Throwable exception = Assertions.assertThrows(ErroCadastroEnderecoException.class, () -> {

            Endereco endereco = EnderecoCriacao.criarEndereco();
            endereco.setCidade(null);

            Mockito.doNothing().when(enderecoServiceImp).validarCep(endereco.getCep());

            enderecoServiceImp.validar(endereco);

        });
        Assertions.assertEquals("Informe uma Cidade", exception.getMessage());
    }

    @Test
    public void ErroDeValidaoPorEstadoVazio() {

        Throwable exception = Assertions.assertThrows(ErroCadastroEnderecoException.class, () -> {
            Endereco endereco = EnderecoCriacao.criarEndereco();
            endereco.setEstado(null);

            enderecoServiceImp.validar(endereco);

        });
        Assertions.assertEquals("Informe um Estado", exception.getMessage());
    }

    @Test
    public void ErroDeValidaoPorCepVazio() {

        Throwable exception = Assertions.assertThrows(ErroCadastroEnderecoException.class, () -> {
            Endereco endereco = EnderecoCriacao.criarEndereco();
            endereco.setCep(null);

            enderecoServiceImp.validar(endereco);

        });
        Assertions.assertEquals("Informe um CEP", exception.getMessage());
    }

    @Test
    public void ErroDeValidaoPorTamanhoDeCepErrado() {

        Throwable exception = Assertions.assertThrows(ErroCadastroEnderecoException.class, () -> {
            Endereco endereco = EnderecoCriacao.criarEndereco();
            endereco.setCep("123456789");

            enderecoServiceImp.validar(endereco);

        });
        Assertions.assertEquals("Tamanho do CEP invalido", exception.getMessage());
    }

    @Test
    public void ErroDeValidaoPorFormatoDeCepInvalido() {

        Throwable exception = Assertions.assertThrows(ErroCadastroEnderecoException.class, () -> {
            Endereco endereco = EnderecoCriacao.criarEndereco();
            endereco.setCep("01001-000");

            enderecoServiceImp.validar(endereco);

        });
        Assertions.assertEquals("Informe um CEP Valido sem (-)", exception.getMessage());
    }



}
