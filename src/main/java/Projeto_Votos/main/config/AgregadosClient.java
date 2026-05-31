package Projeto_Votos.main.config;

import Projeto_Votos.main.dtos.MunicipioDTO;
import Projeto_Votos.main.entity.Municipio;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.lang.reflect.Method;
import java.util.List;

@FeignClient(name = "agregado2022-client", url = "https://servicodados.ibge.gov.br/api/v3/agregados/9514/periodos/2022/variaveis/")
public interface AgregadosClient {

    @RequestMapping(method = RequestMethod.GET, value = "93?localidades=N6[N3[{id_Municipio}]]")
    List<MunicipioDTO> obterPopulacaoCidades(@PathVariable Long id_Municipio);
}
