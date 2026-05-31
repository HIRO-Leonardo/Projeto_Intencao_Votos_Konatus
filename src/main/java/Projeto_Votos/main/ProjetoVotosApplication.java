package Projeto_Votos.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class ProjetoVotosApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProjetoVotosApplication.class, args);
    }
}
