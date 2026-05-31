package Projeto_Votos.main.entity;

import Projeto_Votos.main.dtos.EstadoDTO;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;

@Entity
@Table(name = "estado")
public class Estado {

    @Id
    private String sigla;

    private Long id;

    @JsonProperty("nome")
    private String nome;

    public Estado(String nome, String sigla, Long id) {
        this.sigla = sigla;
        this.id = id;
        this.nome = nome;


    }

    public Estado() {

    }

    public Estado(EstadoDTO dto) {
        this.sigla = dto.getSigla();
        this.id = dto.getId();
        this.nome = dto.getNome();

    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }


}