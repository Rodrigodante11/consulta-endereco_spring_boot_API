package com.rodrigo.consulta_endereco.model.entity;

import com.rodrigo.consulta_endereco.model.enums.Regiao;
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

    @Column(name="cep", unique = true)
    private String cep;

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
    @Enumerated(value =  EnumType.STRING )
    private Regiao regiao;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Endereco endereco = (Endereco) o;
        return Objects.equals(id, endereco.id) && cep.equals(endereco.cep) && Objects.equals(rua, endereco.rua) && Objects.equals(complemento, endereco.complemento) && Objects.equals(bairro, endereco.bairro) && cidade.equals(endereco.cidade) && estado.equals(endereco.estado) && regiao == endereco.regiao;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, cep, rua, complemento, bairro, cidade, estado, regiao);
    }
}
