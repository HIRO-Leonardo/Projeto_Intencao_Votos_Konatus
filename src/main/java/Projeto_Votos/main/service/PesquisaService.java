package Projeto_Votos.main.service;

import Projeto_Votos.main.dtos.ResultadoCandidatoDTO;
import Projeto_Votos.main.entity.Candidato;
import Projeto_Votos.main.entity.IntencaoVoto;
import Projeto_Votos.main.entity.Municipio;
import Projeto_Votos.main.exceptions.ExceptionHandlerSistema;
import Projeto_Votos.main.repository.CandidatoRespository;
import Projeto_Votos.main.repository.IntencaoVotosRepository;
import Projeto_Votos.main.repository.MunicipioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PesquisaService {

    private final MunicipioRepository municipioRepository;
    private final IntencaoVotosRepository intencaoVotosRepository;
    private final CandidatoRespository candidatoRespository;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public PesquisaService(MunicipioRepository municipioRepository, IntencaoVotosRepository intencaoVotosRepository, CandidatoRespository candidatoRespository) {
        this.municipioRepository = municipioRepository;
        this.intencaoVotosRepository = intencaoVotosRepository;
        this.candidatoRespository = candidatoRespository;
    }
    @Transactional
    public void processarCsv(MultipartFile arquivo) throws Exception {

        // 1. CARGA RÁPIDA: Busca cidades e cria chave por "NOME-UF"
        List<Municipio> todasAsCidades = municipioRepository.findAll();
        Map<String, Municipio> mapaCidades = new HashMap<>();

        for (Municipio m : todasAsCidades) {
            String chaveBanco = (m.getNome_cidade())
                    .trim();
            mapaCidades.put(chaveBanco, m);
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(arquivo.getInputStream()))) {
            String linha;
            boolean primeiraLinha = true;

            while ((linha = br.readLine()) != null) {
                if (primeiraLinha) {
                    primeiraLinha = false;
                    continue;
                }

                // Ordem: id_pesquisa, data, municipio, estado, id_candidato, qtd_votos
                String[] colunas = linha.split(",");
                if (colunas.length < 7) continue;

                String idPesquisa = colunas[0].trim();
                LocalDate data = LocalDate.parse(colunas[1].trim(), formatter);
                String nomeMun = colunas[2].trim();
                Integer idCandidato = Integer.parseInt(colunas[4].trim());
                String nomeCandidato = colunas[5].trim();
                Integer qtdVotos = Integer.parseInt(colunas[6].trim());

                Municipio cidade = mapaCidades.get(nomeMun);
                System.out.println("Processando linha: " + nomeMun + " - Votos: " + qtdVotos);
                if (cidade == null) {
                    throw new ExceptionHandlerSistema("Município " + nomeMun + " não mapeado para importação.");
                }

                double participacaoNaCidade = (qtdVotos.doubleValue() / cidade.getPopulacao()) * 100;

                double resultadoPonderado = qtdVotos * participacaoNaCidade;
                Long resultadoArredondado = Math.round(resultadoPonderado);
                IntencaoVoto novoVoto = new IntencaoVoto(
                        idPesquisa,
                        data,
                        idCandidato,
                        qtdVotos,
                        resultadoArredondado,
                        participacaoNaCidade,      // <--- AQUI ESTÁ O QUE VOCÊ QUERIA!
                        cidade,
                        nomeCandidato
                );

                intencaoVotosRepository.saveAndFlush(novoVoto);
            }
        }
        System.out.println("--- Processamento e gravação concluídos com sucesso! ---");
    }

    public List<ResultadoCandidatoDTO> gerarRelatorioFinal() {
        System.out.println("Buscando resultados da eleição no banco de dados...");
        return intencaoVotosRepository.obterResultadoGeral();
    }
}