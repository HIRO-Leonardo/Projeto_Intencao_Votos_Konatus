package Projeto_Votos.main.config;

import Projeto_Votos.main.dtos.EstadoDTO;
import Projeto_Votos.main.dtos.MunicipioDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(name = "igbe-client", url = "https://servicodados.ibge.gov.br/api/v1/")
public interface IgbeConfig {
    @RequestMapping(method = RequestMethod.GET, value = "localidades/estados")
    List<EstadoDTO> obterEstados();

    @RequestMapping(method = RequestMethod.GET, value = "localidades/estados/{UF}/municipios")
    List<MunicipioDTO> obterMunicipioPorEstado(@PathVariable String UF);

}
