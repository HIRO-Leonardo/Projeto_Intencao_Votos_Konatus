package Projeto_Votos.main.dtos;

import com.google.gson.annotations.SerializedName;

public class EstadoDTO {

    @SerializedName("sigla")
    private String sigla;

    @SerializedName("nome")
    private String nome;

    private Long id;



    public EstadoDTO(String nome, String sigla, Long id) {
        this.nome = nome;
        this.sigla = sigla;
        this.id = id;

    }

    public EstadoDTO(){
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
