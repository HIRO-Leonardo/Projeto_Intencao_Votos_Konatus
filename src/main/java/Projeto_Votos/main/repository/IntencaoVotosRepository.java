package Projeto_Votos.main.repository;

import Projeto_Votos.main.dtos.ResultadoCandidatoDTO;
import Projeto_Votos.main.entity.IntencaoVoto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IntencaoVotosRepository extends JpaRepository<IntencaoVoto, Long> {

    @Query("SELECT new Projeto_Votos.main.dtos.ResultadoCandidatoDTO(" +
            "v.nome_candidato, SUM(v.votosBrutos), SUM(v.votosPonderados), v.idCandidato, v.dataPesquisa, v.idPesquisa) " +
            "FROM IntencaoVoto v " +
            "GROUP BY v.nome_candidato, v.idCandidato, v.dataPesquisa, v.idPesquisa " +
            "ORDER BY SUM(v.votosPonderados) DESC")
    List<ResultadoCandidatoDTO> obterResultadoGeral();
}
