package Projeto_Votos.main.controller;

import Projeto_Votos.main.dtos.ResultadoCandidatoDTO;
import Projeto_Votos.main.service.PesquisaService;
import Projeto_Votos.main.service.VirusScannerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/pesquisas")
public class PesquisaController {
    private final PesquisaService pesquisaService;
    private final VirusScannerService virusScannerService;

    public PesquisaController(PesquisaService pesquisaService, VirusScannerService virusScannerService) {
        this.pesquisaService = pesquisaService;
        this.virusScannerService = virusScannerService;
    }

    @PostMapping("/upload-csv")
    public ResponseEntity<String> receberArquivo(@RequestParam("arquivo") MultipartFile arquivo){
        if (arquivo == null || arquivo.isEmpty()) {
            return ResponseEntity.badRequest().body("Erro: Você não enviou nenhum arquivo ou ele está vazio.");
        }

        // Blindagem 2: Garantir que é um arquivo .csv e não uma imagem ou PDF
        String nomeArquivo = arquivo.getOriginalFilename();
        if (nomeArquivo == null || !nomeArquivo.toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Erro: Formato inválido. Por favor, envie apenas planilhas .csv.");
        }

        try {
            virusScannerService.estaInfectado(arquivo);
            return ResponseEntity.ok("Arquivo processado com sucesso!");

        } catch (IllegalArgumentException e) {
            // Se o Service reclamar de algo (ex: CSV sem dados), devolvemos o aviso para a tela
            return ResponseEntity.badRequest().body(e.getMessage());

        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro inesperado no servidor: " + e.getMessage());
        }
    }

    @GetMapping("/resultados")
    public ResponseEntity<List<ResultadoCandidatoDTO>> verResultados() {
        List<ResultadoCandidatoDTO> ranking = pesquisaService.gerarRelatorioFinal();
        return ResponseEntity.ok(ranking);
    }
}
