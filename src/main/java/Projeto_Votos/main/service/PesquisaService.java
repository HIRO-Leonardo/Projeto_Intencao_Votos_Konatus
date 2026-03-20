package Projeto_Votos.main.service;

import Projeto_Votos.main.dtos.ResultadoCandidatoDTO;
import Projeto_Votos.main.entity.Candidato;
import Projeto_Votos.main.entity.IntencaoVoto;
import Projeto_Votos.main.entity.Municipio;
import Projeto_Votos.main.repository.CandidatoRespository;
import Projeto_Votos.main.repository.IntencaoVotosRepository;
import Projeto_Votos.main.repository.MunicipioRepository;
import org.springframework.stereotype.Service;
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

    public void processarCsv(MultipartFile arquivo) throws Exception {

        // 1. CARGA RÁPIDA: Busca cidades e cria chave por "NOME-UF"
        List<Municipio> todasAsCidades = municipioRepository.findAll();
        Map<String, Municipio> mapaCidades = new HashMap<>();

        for (Municipio m : todasAsCidades) {
            // Criamos uma chave única combinando nome e estado em maiúsculas para evitar erros
            String chaveBanco = (m.getNome_cidade())
                    .toUpperCase()
                    .replace(" ", "")
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

                // Ordem: id_pesquisa; data; municipio; estado; id_candidato; qtd_votos
                String[] colunas = linha.split(";");
                if (colunas.length < 7) continue;

                try {
                    String idPesquisa = colunas[0].trim();
                    LocalDate data = LocalDate.parse(colunas[1].trim(), formatter);
                    String nomeMun = colunas[2].trim().toUpperCase();

                    Integer idCandidato = Integer.parseInt(colunas[4].trim());
                    Integer qtdVotos = Integer.parseInt(colunas[6].trim());
                    String nomeCandidato = colunas[5].trim();
                    // 2. BUSCA A CIDADE PELO NOME E UF
                    String buscaChave = nomeMun ;

                    Candidato candidato = new Candidato(idCandidato,nomeCandidato);
                    candidatoRespository.save(candidato);
                    Municipio cidade = mapaCidades.get(buscaChave);

                    if (cidade != null) {
                        // 3. CÁLCULO PONDERADO
                        double peso = calcularPeso(cidade.getGrupo().name());
                        double resultadoPonderado = qtdVotos * peso;

                        // 4. SALVA COM OS NOVOS CAMPOS
                        IntencaoVoto novoVoto = new IntencaoVoto(
                                idPesquisa,
                                data,
                                idCandidato,
                                qtdVotos,
                                resultadoPonderado,
                                cidade,
                                nomeCandidato

                        );
                        intencaoVotosRepository.save(novoVoto);

                    } else {
                        System.out.println("Aviso: Município " + buscaChave + " não encontrado no banco.");
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao processar linha: " + linha + " | Erro: " + e.getMessage());
                }
            }
        }


        System.out.println("--- Processamento e gravação concluídos com sucesso! ---");
    }

    private double calcularPeso(String grupo) {
        switch (grupo) {
            case "GRUPO_1": return 1.0;
            case "GRUPO_2": return 1.5;
            case "GRUPO_3": return 2.0;
            case "GRUPO_4": return 2.5;
            default: return 1.0;
        }
    }

    public List<ResultadoCandidatoDTO> gerarRelatorioFinal() {
        System.out.println("Buscando resultados da eleição no banco de dados...");
        return intencaoVotosRepository.obterResultadoGeral();
    }
}