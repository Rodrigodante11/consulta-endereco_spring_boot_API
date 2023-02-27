package com.rodrigo.consulta_endereco.model.repository;

import com.rodrigo.consulta_endereco.model.entity.Endereco;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EnderecoRepository extends JpaRepository<Endereco, Long> {

}
