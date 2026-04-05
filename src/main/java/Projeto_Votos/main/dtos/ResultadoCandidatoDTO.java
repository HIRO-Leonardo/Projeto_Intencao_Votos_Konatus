package Projeto_Votos.main.dtos;

import java.time.LocalDate;

public class ResultadoCandidatoDTO {
    private String nomeCandidato;
    private Long totalVotosBrutos;
    private Long totalVotosPonderados;
    private Integer idCandidato;
    private LocalDate dataPesquisa;
    private String idPesquisa;

    public ResultadoCandidatoDTO(String nomeCandidato, Long totalVotosBrutos, Long totalVotosPonderados, Integer idCandidato, LocalDate dataPesquisa, String idPesquisa) {
        this.nomeCandidato = nomeCandidato;
        this.totalVotosBrutos = totalVotosBrutos;
        this.totalVotosPonderados = totalVotosPonderados;
        this.idCandidato = idCandidato;
        this.dataPesquisa = dataPesquisa;
        this.idPesquisa = idPesquisa;
    }

    // Getters

    public String getNomeCandidato() {
        return nomeCandidato;
    }

    public void setNomeCandidato(String nomeCandidato) {
        this.nomeCandidato = nomeCandidato;
    }

    public Long getTotalVotosBrutos() {
        return totalVotosBrutos;
    }

    public void setTotalVotosBrutos(Long totalVotosBrutos) {
        this.totalVotosBrutos = totalVotosBrutos;
    }

    public Long getTotalVotosPonderados() {
        return totalVotosPonderados;
    }

    public void setTotalVotosPonderados(Long totalVotosPonderados) {
        this.totalVotosPonderados = totalVotosPonderados;
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

    public String getIdPesquisa() {
        return idPesquisa;
    }

    public void setIdPesquisa(String idPesquisa) {
        this.idPesquisa = idPesquisa;
    }
}
