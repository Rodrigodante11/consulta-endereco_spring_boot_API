package com.rodrigo.consulta_endereco.model.entity;

import lombok.*;
import javax.persistence.*;
import java.util.Objects;


//@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table( name="endereco", schema = "consulta_endereco")
@Entity
public class Endereco {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // deixei para o banco dizer que eh auto incremento (bigserial postgre)
    private Long id;

    @Column(unique = true)
    private String cep;

    private String rua;

    private String complemento;

    private String bairro;

    private String cidade;

    private String estado;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(id, endereco.id) && cep.equals(endereco.cep) && Objects.equals(rua, endereco.rua)
                && Objects.equals(complemento, endereco.complemento) && Objects.equals(bairro, endereco.bairro)
                && cidade.equals(endereco.cidade) && estado.equals(endereco.estado) ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cep, rua, complemento, bairro, cidade, estado);
    }
}
