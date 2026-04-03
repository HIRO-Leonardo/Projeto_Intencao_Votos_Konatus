package Projeto_Votos.main.service;

import io.sensesecure.clamav4j.ClamAV;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.net.InetSocketAddress;

@Service
public class VirusScannerService {
    private static final Logger log = LogManager.getLogger(VirusScannerService.class);
    private final PesquisaService pesquisaService;

    public VirusScannerService(PesquisaService pesquisaService) {
        this.pesquisaService = pesquisaService;
    }


    public void estaInfectado(MultipartFile file)  {
        String nomeArquivo = file.getOriginalFilename();
        ClamAV clamAV = new ClamAV(new InetSocketAddress("localhost", 3310), 5000);
        log.info("Iniciando scan de segurança para o arquivo: {}", nomeArquivo);
        log.debug("Conectando ao servidor ClamAV em localhost:3310...");
        try {
           boolean isClear = Boolean.parseBoolean(clamAV.scan(file.getInputStream()));

           if (isClear){
               System.out.println("Arquivo aprovado! Sem vírus.");
               log.info("Arquivo {} verificado e está LIMPO.", nomeArquivo);
               pesquisaService.processarCsv(file);
           }else {
               log.warn("PERIGO: Vírus detectado no arquivo: {}", nomeArquivo);
               throw new SecurityException("Arquivo malicioso bloqueado!");
           }

        } catch (java.net.ConnectException ex){

            log.error("AVISO: ClamAV não encontrado. Continuando sem scan para fins de teste local.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
