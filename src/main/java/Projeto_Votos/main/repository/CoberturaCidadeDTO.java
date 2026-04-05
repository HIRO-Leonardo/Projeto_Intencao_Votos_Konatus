package Projeto_Votos.main.repository;

import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CoberturaCidadeDTO {
    String getCidade();
    Long getTotalVotos();
    Long getPopulacaoIbge();
    Double getPercentualParticipacao();

    @Query(value = "SELECT m.nome as cidade, " +
            "m.populacao as populacaoIbge, " +
            "COUNT(v.id) as totalVotos, " +
            "ROUND((COUNT(v.id) * 100.0) / m.populacao, 4) as percentualParticipacao " +
            "FROM municipio m " +
            "LEFT JOIN voto v ON v.municipio_id = m.id " +
            "WHERE m.id = :municipioId " +
            "GROUP BY m.id, m.nome, m.populacao", nativeQuery = true)
    Optional<CoberturaCidadeDTO> obterCoberturaPorCidade(Long municipioId);
}
