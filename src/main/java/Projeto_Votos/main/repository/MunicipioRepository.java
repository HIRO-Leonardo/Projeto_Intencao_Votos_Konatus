package Projeto_Votos.main.repository;

import Projeto_Votos.main.entity.Municipio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface MunicipioRepository extends JpaRepository<Municipio,Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Municipio m SET m.populacao = :populacao WHERE m.id = :id")
    void updatePopulacaoById(Long id, int populacao);
}
