package Projeto_Votos.main.entity;

import Projeto_Votos.main.enums.GrupoMunicipio;
import jakarta.persistence.*;
@Entity
@Table(name = "municipio")
public class Municipio {
    @Id
    private Long id;
    private String nome_cidade;
    private Integer populacao;

    @Enumerated(EnumType.STRING)
    private GrupoMunicipio grupoMunicipio;

    @ManyToOne
    @JoinColumn(name = "estado_sigla")
    private Estado estado;

    public Municipio(Long id, String nome_cidade, Integer populacao, Estado estado, GrupoMunicipio grupoMunicipio) {
        this.id = id;
        this.nome_cidade = nome_cidade;
        this.populacao = populacao;
        this.estado = estado;
        this.grupoMunicipio = grupoMunicipio;
    }

    public Municipio() {
    }

    public GrupoMunicipio getGrupoMunicipio() {
        return grupoMunicipio;
    }

    public void setGrupoMunicipio(GrupoMunicipio grupoMunicipio) {
        this.grupoMunicipio = grupoMunicipio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome_cidade() {
        return nome_cidade;
    }

    public void setNome_cidade(String nome_cidade) {
        this.nome_cidade = nome_cidade;
    }

    public Integer getPopulacao() {
        return populacao;
    }

    public void setPopulacao(Integer populacao) {
        this.populacao = populacao;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public GrupoMunicipio calcularGrupoPelaPopulacao(Integer populacao){
        if (populacao == null || populacao == 0) {
            return GrupoMunicipio.GRUPO_0;
        }
        if (populacao <= RegraGruposPopulacao.LIMITE_GRUPO_1){return GrupoMunicipio.GRUPO_1;}
        if (populacao <= RegraGruposPopulacao.LIMITE_GRUPO_2){return GrupoMunicipio.GRUPO_2;}
        if (populacao <= RegraGruposPopulacao.LIMITE_GRUPO_3){return GrupoMunicipio.GRUPO_3;}

        return GrupoMunicipio.GRUPO_4;
    }


}
