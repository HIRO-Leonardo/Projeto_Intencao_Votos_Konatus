package Projeto_Votos.main.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.time.Duration;

@Configuration
public class ApiIbgeConfig {
    @Bean
    public RestClient restClient(RestClient.Builder builder){
        JdkClientHttpRequestFactory factory = new JdkClientHttpRequestFactory();
        factory.setReadTimeout(Duration.ofSeconds(120));



        return builder.baseUrl("https://servicodados.ibge.gov.br/api/v1").requestFactory(factory).build();
    }
}
