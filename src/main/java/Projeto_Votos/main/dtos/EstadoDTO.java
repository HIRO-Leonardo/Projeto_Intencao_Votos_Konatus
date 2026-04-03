package Projeto_Votos.main.dtos;

import Projeto_Votos.main.entity.Municipio;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.Id;

import java.util.List;

public class EstadoDTO {

    @SerializedName("sigla")
    private String sigla;

    @SerializedName("nome")
    private String nome_estado;

    private List<Municipio> municipios;

    @SerializedName("ibgeid")
    private Long idIbge;

    public EstadoDTO(String nome_estado, String sigla, List<Municipio> municipios, Long idIbge) {
        this.nome_estado = nome_estado;
        this.sigla = sigla;
        this.municipios = municipios;
        this.idIbge = idIbge;
    }

    public EstadoDTO(){

    }

    public Long getIdIbge() {
        return idIbge;
    }

    public void setIdIbge(Long idIbge) {
        this.idIbge = idIbge;
    }

    public void addMunicipio(Municipio municipio){
        this.municipios.add(municipio);
    }

    public Integer getPopulacao(){
        Integer total = 0;
        for (Municipio m: municipios){
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
