package com.rodrigo.consulta_endereco.model.repository;

import com.rodrigo.consulta_endereco.model.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {
    Optional<Endereco> findByCep(String numero_cep);
}
