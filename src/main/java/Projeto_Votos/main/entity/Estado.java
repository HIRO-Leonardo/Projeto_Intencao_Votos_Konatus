package Projeto_Votos.main.entity;

import Projeto_Votos.main.entity.Municipio;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;
@Entity
@Table(name = "estado")
public class Estado {
    @Id
    private String sigla;

    private String nome_estado;
    @OneToMany(mappedBy = "estado")
    private List<Municipio> municipios;

    public Estado(String nome_estado, String sigla, List<Municipio> municipios) {
        this.nome_estado = nome_estado;
        this.sigla = sigla;
        this.municipios = municipios;
    }

    public Estado() {

    }


    public void addMunicipio(Municipio municipio) {
        this.municipios.add(municipio);
    }

    public Integer getPopulacao() {
        Integer total = 0;
        for (Municipio m : municipios) {
            total += m.getPopulacao();
        }
        return total;
    }

    public String getNome_estado() {
        return nome_estado;
    }

    public void setNome_estado(String nome_estado) {
        this.nome_estado = nome_estado;
    }

    public String getSigla() {
        return sigla;
    }

    public void setSigla(String sigla) {
        this.sigla = sigla;
    }

    public List<Municipio> getMunicipios() {
        return municipios;
    }

    public void setMunicipios(List<Municipio> municipios) {
        this.municipios = municipios;
    }
}