package Projeto_Votos.main.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "candidato")
public class Candidato {
    @Id
    private Integer id;

    @Column(name = "nome")
    private String nome;

    public Candidato(Integer id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Candidato() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
