package Projeto_Votos.main.controller;

import Projeto_Votos.main.service.IbgeService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ibge")
public class IbgeController {
    private final IbgeService ibgeService;


    public IbgeController(IbgeService ibgeService) {
        this.ibgeService = ibgeService;
    }

    @GetMapping("/sincronizar")
    public String sincronizar(){
        System.out.println("Recebido para a requisicao para sincronizar dados do IBGE");
        ibgeService.sincronizar();
        return "Sincronização com o IBGE finalizada!";
    }
}
