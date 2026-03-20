package Projeto_Votos.main.entity;

import jakarta.persistence.*;

import java.time.LocalDate;


@Entity
@Table(name = "intencao_voto")
public class IntencaoVoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_pesquisa")
    private String idPesquisa; // Usamos String caso o ID tenha letras (ex: "PESQ-101")

    @Column(name = "data_pesquisa")
    private LocalDate dataPesquisa; // Formato oficial de data no Java

    @Column(name = "id_candidato")
    private Integer idCandidato; // Mudamos de "nome" para "ID" conforme a imagem

    @Column(name = "quantidade_votos")
    private Integer votosBrutos; // Os votos originais da planilha

    @Column(name = "votos_ponderados")
    private Double votosPonderados; // O resultado do nosso cálculo

    @Column(name = "nome_candidato")
    private String nome_candidato;


    @ManyToOne
    @JoinColumn(name = "municipio_id")
    private Municipio municipio;

    // Construtor vazio obrigatório do Hibernate
    public IntencaoVoto() {}

    // Construtor completo atualizado
    public IntencaoVoto(String idPesquisa, LocalDate dataPesquisa, Integer idCandidato, Integer votosBrutos, Double votosPonderados, Municipio municipio, String nome_candidato) {
        this.idPesquisa = idPesquisa;
        this.dataPesquisa = dataPesquisa;
        this.idCandidato = idCandidato;
        this.votosBrutos = votosBrutos;
        this.votosPonderados = votosPonderados;
        this.municipio = municipio;
        this.nome_candidato = nome_candidato;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public Integer getVotosBrutos() {
        return votosBrutos;
    }

    public void setVotosBrutos(Integer votosBrutos) {
        this.votosBrutos = votosBrutos;
    }

    public String getNome_candidato() {
        return nome_candidato;
    }

    public void setNome_candidato(String nome_candidato) {
        this.nome_candidato = nome_candidato;
    }



    public Double getVotosPonderados() {
        return votosPonderados;
    }

    public void setVotosPonderados(Double votosPonderados) {
        this.votosPonderados = votosPonderados;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public String getIdPesquisa() {
        return idPesquisa;
    }

    public void setIdPesquisa(String idPesquisa) {
        this.idPesquisa = idPesquisa;
    }

    public LocalDate getDataPesquisa() {
        return dataPesquisa;
    }

    public void setDataPesquisa(LocalDate dataPesquisa) {
        this.dataPesquisa = dataPesquisa;
    }

    public Integer getIdCandidato() {
        return idCandidato;
    }

    public void setIdCandidato(Integer idCandidato) {
        this.idCandidato = idCandidato;
    }
}
