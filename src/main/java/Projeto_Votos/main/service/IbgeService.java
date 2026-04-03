package Projeto_Votos.main.service;


import Projeto_Votos.main.dtos.MunicipioDTO;
import Projeto_Votos.main.entity.Estado;
import Projeto_Votos.main.entity.Municipio;
import Projeto_Votos.main.repository.EstadoRepository;
import Projeto_Votos.main.repository.MunicipioRepository;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.*;


@Service
public class IbgeService {

    private final EstadoRepository estadoRepository;

    private final MunicipioRepository municipioRepository;

    private final RestClient restClient;

    public IbgeService(EstadoRepository estadoRepository, MunicipioRepository municipioRepository, RestClient restClient) {
        this.estadoRepository = estadoRepository;
        this.municipioRepository = municipioRepository;
        this.restClient = restClient;

    }
    @Transactional
    @CircuitBreaker(name = "DadosIBGE", fallbackMethod = "ProcessarDadosIBGEFallback")
    public List<Estado> sincronizar(){


        try {
            List<Estado> estados = restClient.get()
                    .uri("/localidades/estados")
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, ((request, response) ->{
                        throw new RuntimeException("Erro API do IBGE:" + response.getStatusCode());

                    }))
                    .body(new ParameterizedTypeReference<List<Estado>>() {}
                    );

            if (estados != null && !estados.isEmpty()){
                List<Estado> estadoSalvos = estadoRepository.saveAllAndFlush(estados);

                sincronizarMunicipiosEstado(estadoSalvos);
                atualizarPopulacaoCidades(estadoSalvos);
                return estadoSalvos;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);

        }
        return Collections.emptyList();
    }

    public void sincronizarMunicipiosEstado(List<Estado> estados) {
        Random random = new Random();
        for (Estado estado1 : estados) {
            try {

                List<MunicipioDTO> municipioDTOS = restClient.get()
                        .uri("/localidades/estados/" + estado1.getSigla() + "/municipios")
                        .retrieve()
                        .onStatus(HttpStatusCode::isError, ((request, response) -> {
                            throw new RuntimeException("Erro API do IBGE:" + response.getStatusCode());

                        }))
                        .body(new ParameterizedTypeReference<List<MunicipioDTO>>() {
                              }
                        );
                Map<Long, Integer> dadosPopulacao = buscarPopulacaoRealDoEstado(estado1.getIdIbge());
                if (municipioDTOS != null && !municipioDTOS.isEmpty()) {

                    List<Municipio> municipioList = new ArrayList<>();

                    for (MunicipioDTO dto : municipioDTOS) {

                        int populacaoReal = dadosPopulacao.getOrDefault(dto.getId(), 0);
                        Municipio municipio = new Municipio(dto.getId(), dto.getNome(), populacaoReal, estado1);
                        municipioList.add(municipio);
                    }

                    municipioRepository.saveAll(municipioList);
                }
            }catch(Exception e){
            throw new RuntimeException(e);
        }
        }
    }
    public void atualizarPopulacaoCidades(List<Estado> estados){
        for (Estado estado: estados){
            try {
                JsonNode response = restClient.get()
                        .uri("https://servicodados.ibge.gov.br/api/v3/agregados/9514/periodos/2022/variaveis/93?localidades=N6[N3[" + estado.getIdIbge() + "]]")
                        .retrieve()
                        .body(JsonNode.class);
                JsonNode series = response.get(0).get("resultados").get(0).get("series");

                for (JsonNode node: series){
                    Long ibgeId = node.get("localidade").get("id").asLong();
                    int populacaoReal = node.get("serie").get("2022").asInt();
                    System.out.println("Atualizando ID " + ibgeId + " com populacao " + populacaoReal);
                    municipioRepository.updatePopulacaoById(ibgeId, populacaoReal);
                }
                System.out.println("População real atualizada para o estado: " + estado.getSigla());
            }catch (Exception e){
                System.err.println("Erro ao obter população de " + estado.getIdIbge() + ": " + e.getMessage());
            }
        }
    }
    private Map<Long, Integer> buscarPopulacaoRealDoEstado(Long estadoId) {
        try {
            // API de Agregados (Censo 2022) - Tabela 9514, Variável 93 (População)
            JsonNode response = restClient.get()
                    .uri("https://servicodados.ibge.gov.br/api/v3/agregados/9514/periodos/2022/variaveis/93?localidades=N6[N3[" + estadoId + "]]")
                    .retrieve()
                    .body(JsonNode.class);

            Map<Long, Integer> mapaPopulacao = new HashMap<>();
            JsonNode series = response.get(0).get("resultados").get(0).get("series");

            for (JsonNode node : series) {
                Long municipioId = node.get("localidade").get("id").asLong();
                int valor = node.get("serie").get("2022").asInt();
                mapaPopulacao.put(municipioId, valor);

            }
            return mapaPopulacao;
        } catch (Exception e) {
            System.err.println("Erro ao buscar população real: " + e.getMessage());
            return Collections.emptyMap(); // Retorna vazio se falhar
        }
    }

}
