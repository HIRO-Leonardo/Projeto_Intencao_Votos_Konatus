package Projeto_Votos.main.service;

import Projeto_Votos.main.dtos.EstadoDTO;
import Projeto_Votos.main.dtos.MunicipioDTO;
import Projeto_Votos.main.entity.Estado;
import Projeto_Votos.main.entity.Municipio;
import Projeto_Votos.main.entity.RegraGruposPopulacao;
import Projeto_Votos.main.repository.EstadoRepository;
import Projeto_Votos.main.repository.MunicipioRepository;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.springframework.stereotype.Service;


import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class IbgeService {

    private final EstadoRepository estadoRepository;

    private final MunicipioRepository municipioRepository;

    private final HttpClient httpClient;

    private final Gson gson;


    public IbgeService(EstadoRepository estadoRepository, MunicipioRepository municipioRepository) {
        this.estadoRepository = estadoRepository;
        this.municipioRepository = municipioRepository;
        this.httpClient = HttpClient.newHttpClient();
        this.gson = new Gson();
    }

    public void sincronizar(){
        System.out.println("Iniciando sincronização com o IBGE!!!");

        try {
            HttpRequest requestEstados = HttpRequest.newBuilder()
                    .uri(URI.create("https://servicodados.ibge.gov.br/api/v1/localidades/estados"))
                    .GET()
                    .build();
            HttpResponse<String> responseEstados = httpClient.send(requestEstados, HttpResponse.BodyHandlers.ofString());
            Type listTipoEstado = new TypeToken<List<EstadoDTO>>(){}.getType();
            List<EstadoDTO> estadoDTOS = gson.fromJson(responseEstados.body(),listTipoEstado);

            for (EstadoDTO dto : estadoDTOS){
               Estado estado = new Estado(dto.getNome_estado(),dto.getSigla(), new ArrayList<>());
                estadoRepository.save(estado);
                System.out.println("Buscando municipio de:" + estado.getSigla());
                sincronizarMunicipiosEstado(estado);

            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void sincronizarMunicipiosEstado(Estado estado){
        try{
            HttpRequest requestMunicipios = HttpRequest.newBuilder()
                    .uri(URI.create("https://servicodados.ibge.gov.br/api/v1/localidades/estados/" + estado.getSigla() + "/municipios"))
                    .GET().build();

            HttpResponse<String> response = httpClient.send(requestMunicipios, HttpResponse.BodyHandlers.ofString());

            Type listTypeMunicipio = new TypeToken<List<MunicipioDTO>>(){}.getType();
            List<MunicipioDTO> municipiosDTO = gson.fromJson(response.body(), listTypeMunicipio);

            Random random = new Random();

            for (MunicipioDTO dto : municipiosDTO) {
                // Simulando uma população entre 10 mil e 1.5 milhões para o teste
                int populacaoSimulada = 10000 + random.nextInt(1490000);
                Municipio municipio = new Municipio(dto.getId(), dto.getNome(), populacaoSimulada, estado);
                municipioRepository.save(municipio); // <-- Mágica do Spring salvando a cidade!
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }
}
