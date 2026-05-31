package Projeto_Votos.main.service;


import Projeto_Votos.main.config.AgregadosClient;
import Projeto_Votos.main.config.IgbeConfig;
import Projeto_Votos.main.dtos.EstadoDTO;
import Projeto_Votos.main.dtos.MunicipioDTO;
import Projeto_Votos.main.entity.Estado;
import Projeto_Votos.main.entity.Municipio;
import Projeto_Votos.main.exceptions.ExceptionHandlerSistema;
import Projeto_Votos.main.repository.EstadoRepository;
import Projeto_Votos.main.repository.MunicipioRepository;

import com.fasterxml.jackson.databind.JsonNode;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.*;


@Service
public class IbgeService {

    @Autowired
    IgbeConfig igbeConfig;

    AgregadosClient agregadosClient;

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
            List<EstadoDTO> estados = igbeConfig.obterEstados();
            if (estados == null || estados.isEmpty()){
                throw new ExceptionHandlerSistema("A lista de estados não pode estar vazia!");
            }
            List<Estado> dtoForEntity = estados.stream()
                    .map(dto -> {
                        System.out.println(dto);
                        return new Estado(dto);
                    })
                    .toList();


            return salvarDadosSincronizados(dtoForEntity);
    }

    @Transactional
    public List<Estado> salvarDadosSincronizados(List<Estado> dtoForEntity) {
        List<Estado> estadoSalvos = estadoRepository.saveAllAndFlush(dtoForEntity);
        sincronizarMunicipiosEstado(estadoSalvos);
        //atualizarPopulacaoCidades(estadoSalvos);
        return estadoSalvos;
    }
    public List<Estado> ProcessarDadosIBGEFallback(Exception e) {
        System.err.println("Circuit Breaker Ativado! Motivo: " + e.getMessage());

        // Aqui, em vez de dar erro 500, você pode retornar o que já tem no banco
        // para o sistema não parar totalmente.
        List<Estado> estadosNoBanco = estadoRepository.findAll();

        if (estadosNoBanco.isEmpty()) {
            throw new ExceptionHandlerSistema("IBGE fora do ar e banco de dados vazio.");
        }

        return estadosNoBanco;
    }

    public void sincronizarMunicipiosEstado(List<Estado> estados) {

        for (Estado estado1 : estados) {
            try {

                List<MunicipioDTO> municipioDTOS = igbeConfig.obterMunicipioPorEstado(estado1.getSigla());
                Map<Long, Integer> dadosPopulacao = buscarPopulacaoRealDoEstado(estado1.getId());
                if (municipioDTOS != null && !municipioDTOS.isEmpty()) {

                    List<Municipio> municipioList = new ArrayList<>();

                    for (MunicipioDTO dto : municipioDTOS) {

                        int populacaoReal = dadosPopulacao.getOrDefault(dto.getId(), 0);

                        //, , , ,
                        Municipio municipio = new Municipio();
                        municipio.setId(dto.getId());
                        municipio.setNome_cidade(dto.getNome());
                        municipio.setPopulacao(populacaoReal);
                        municipio.setEstado(estado1);
                        municipio.setGrupoMunicipio(municipio.calcularGrupoPelaPopulacao(populacaoReal));
                        System.out.println(municipio.getGrupoMunicipio());
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
        /*for (Estado estado: estados){
            try {
                JsonNode response = restClient.get()
                        .uri("https://servicodados.ibge.gov.br/api/v3/agregados/9514/periodos/2022/variaveis/93?localidades=N6[N3[" + estado.getId() + "]]")
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
                System.err.println("Erro ao obter população de " + estado.getId() + ": " + e.getMessage());
            }
        } */
        //List<MunicipioDTO> listaMunicipios = agregadosClient.obterPopulacaoCidades(estado.getId());
        //System.out.println(listaMunicipios);

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
