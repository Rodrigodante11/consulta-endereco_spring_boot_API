package com.rodrigo.consulta_endereco.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table( name="endereco", schema = "consulta_endereco")
public class Endereco {

    @Id
    @Column(name="id")
    private Long cep;

    @Column(name="rua")
    private String rua;
    @Column(name="complemento")
    private String complemento;

    @Column(name="bairro")
    private String bairro;
    @Column(name="cidade")
    private String cidade;
    @Column(name="estado")
    private String estado;
    @Column(name="regiao")
    private Double regiao;
}
