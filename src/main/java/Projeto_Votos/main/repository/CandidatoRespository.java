package Projeto_Votos.main.repository;

import Projeto_Votos.main.entity.Candidato;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CandidatoRespository extends JpaRepository<Candidato, Integer> {
}
