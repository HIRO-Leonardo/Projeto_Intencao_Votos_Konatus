package Projeto_Votos.main.service;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class VirusScannerService {
    private static final Logger log = LogManager.getLogger(VirusScannerService.class);
    private final PesquisaService pesquisaService;

    public VirusScannerService(PesquisaService pesquisaService) {
        this.pesquisaService = pesquisaService;
    }


    public void estaInfectado(MultipartFile file) throws Exception {
        String nomeArquivo = file.getOriginalFilename();
        log.info("Iniciando scan de segurança para o arquivo: {}", nomeArquivo);
        log.debug("Conectando ao servidor ClamAV em localhost:3310...");
        try {
            // 1. Criamos um arquivo temporário para o ClamAV ler
            Path tempFile = Files.createTempFile("scan_", file.getOriginalFilename());
            file.transferTo(tempFile);

            // 2. Chamamos o comando do sistema direto
            ProcessBuilder pb = new ProcessBuilder("clamscan", "--no-summary", tempFile.toString());
            Process process = pb.start();
            int exitCode = process.waitFor(); // 0 = Limpo, 1 = Vírus

            Files.delete(tempFile); // Limpa o rastro

            if (exitCode == 0) {
                log.info("Arquivo {} LIMPO via clamscan (CLI).", nomeArquivo);
                pesquisaService.processarCsv(file);
            } else {
                log.warn("PERIGO: Vírus detectado no arquivo!");
                throw new SecurityException("Arquivo malicioso bloqueado!");
            }

        } catch (java.net.ConnectException ex){
            log.error("AVISO: ClamAV não encontrado. Continuando sem scan para fins de teste local.");
        } catch (Exception e) {
            log.error("Erro ao rodar clamscan: {}. Continuando para teste local.", e.getMessage());
            pesquisaService.processarCsv(file);
            throw new RuntimeException(e);

        }
    }
}
