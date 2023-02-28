package com.rodrigo.consulta_endereco.model.repository;

import com.rodrigo.consulta_endereco.model.entity.Endereco;
import com.rodrigo.consulta_endereco.model.enums.Regiao;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;

import java.util.Optional;

@ExtendWith( SpringExtension.class)
@ActiveProfiles("test")
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)

public class EnderecoRepositoryTest {

    @Autowired
    EnderecoRepository enderecoRepository;

    @Autowired
    TestEntityManager entityManager;

    public static Endereco criarEndereco(){
        return Endereco.builder()
                .cep("37548000")
                .rua("Gilberto da Silva Viana")
                .complemento("Casa")
                .bairro("Chacara")
                .cidade("Conceicao dos ouros")
                .estado("Minas Gerais")
                .regiao(Regiao.SUDESTE).build();
    }

    @Test
    public void criarEPersistirUmEndereco(){
        Endereco endereco = criarEndereco();

        Endereco enderecoSalvo = enderecoRepository.save(endereco);

        boolean result = enderecoRepository.existsById(enderecoSalvo.getId());

        Assertions.assertThat(result).isTrue();

    }

    @Test
    public void deveVerificarAExistenciadeUmCep(){
        Endereco endereco = criarEndereco();

        entityManager.persist(endereco);

        Optional<Endereco> result =  enderecoRepository.findByCep("37548000");

        Assertions.assertThat(result.isPresent()).isTrue();
    }




}
