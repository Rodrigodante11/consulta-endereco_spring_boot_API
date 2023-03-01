package com.rodrigo.consulta_endereco.controller.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodrigo.consulta_endereco.controller.dto.EnderecoDTO;
import com.rodrigo.consulta_endereco.model.entity.Endereco;
import com.rodrigo.consulta_endereco.service.EnderecoService;
import com.rodrigo.consulta_endereco.utils.EnderecoCriacao;
import com.rodrigo.consulta_endereco.utils.EnderecoCriacaoDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Optional;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test") // vai procurar o aplication-test.properties e usar o BD em memoria para teste e nao o oficial
@WebMvcTest(controllers = EnderecoResources.class) // controle que ira ser testado
@AutoConfigureMockMvc
public class EnderecoResourcesTest {

    static final String API= "/v1";

    static final MediaType JSON = MediaType.APPLICATION_JSON;

    @Autowired
    MockMvc mvc;

    @MockBean
    EnderecoService enderecoService;

    @Test
    public void deveCriarUmNovoEndereco() throws Exception{

        EnderecoDTO enderecoDTO = EnderecoCriacaoDTO.criarEnderecoDTO();
        Endereco endereco = EnderecoCriacao.criarEndereco();

        Mockito.when(enderecoService.salvarEndereco(Mockito.any(Endereco.class))).thenReturn(endereco);

        String json = new ObjectMapper().writeValueAsString(enderecoDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                                        .post(API.concat("/criar-endereco"))
                                        .accept(JSON)
                                        .contentType(JSON)
                                        .content(json);

        mvc.perform(request).andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("cep").value(endereco.getCep()))
                .andExpect(MockMvcResultMatchers.jsonPath("rua").value(endereco.getRua()))
                .andExpect(MockMvcResultMatchers.jsonPath("complemento").value(endereco.getComplemento()))
                .andExpect(MockMvcResultMatchers.jsonPath("bairro").value(endereco.getBairro()))
                .andExpect(MockMvcResultMatchers.jsonPath("cidade").value(endereco.getCidade()))
                .andExpect(MockMvcResultMatchers.jsonPath("estado").value(endereco.getEstado()));

    }
    @Test
    public void deveConsultarAPIInternaPorCep() throws Exception{
        EnderecoDTO enderecoDTO = EnderecoCriacaoDTO.criarEnderecoDTO();
        Endereco endereco = EnderecoCriacao.criarEndereco();

        Mockito.when(enderecoService.buscarEnderecoPorCep("37548000")).thenReturn(Optional.of(endereco));

        String json = new ObjectMapper().writeValueAsString(enderecoDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/consulta-endereco"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("cep").value(endereco.getCep()))
                .andExpect(MockMvcResultMatchers.jsonPath("rua").value(endereco.getRua()))
                .andExpect(MockMvcResultMatchers.jsonPath("complemento").value(endereco.getComplemento()))
                .andExpect(MockMvcResultMatchers.jsonPath("bairro").value(endereco.getBairro()))
                .andExpect(MockMvcResultMatchers.jsonPath("cidade").value(endereco.getCidade()))
                .andExpect(MockMvcResultMatchers.jsonPath("estado").value(endereco.getEstado()));

    }
    @Test
    public void deveLancarExcessaoConsultarAPIInternaPorCep() throws Exception{
        EnderecoDTO enderecoDTO = EnderecoCriacaoDTO.criarEnderecoDTO();

        Mockito.when(enderecoService.buscarEnderecoPorCep("12345678")).thenReturn(Optional.empty());

        String json = new ObjectMapper().writeValueAsString(enderecoDTO);

        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post(API.concat("/consulta-endereco"))
                .accept(JSON)
                .contentType(JSON)
                .content(json);

        mvc.perform(request)
                .andExpect(MockMvcResultMatchers.status().isBadRequest());

    }

}
