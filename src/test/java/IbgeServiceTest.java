
import Projeto_Votos.main.repository.EstadoRepository;
import Projeto_Votos.main.repository.MunicipioRepository;
import Projeto_Votos.main.service.IbgeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Habilita o Mockito no JUnit 5
class IbgeServiceTest {

    @Mock
    private HttpClient httpClient; // Simula o cliente que vai no IBGE

    @Mock
    private EstadoRepository estadoRepository;

    @Mock
    private MunicipioRepository municipioRepository;

    @InjectMocks
    private IbgeService ibgeService; // Injeta o Mock acima dentro do seu serviço

    @Test
    @DisplayName("Deve retornar lista vazia quando o IBGE estiver fora do ar")
    void deveRetornarVazioQuandoApiFalha() throws Exception {
        // Simula uma exceção de conexão (os famosos 30 segundos)
        lenient().when(httpClient.send(any(), any())).thenThrow(new RuntimeException("Erro de Conexão"));

        // Executa o método do seu serviço
        var resultado = ibgeService.sincronizar();

        // Verifica se o seu código tratou o erro e retornou algo seguro
        assertNotNull(resultado);
        assertTrue(resultado.isEmpty(), "A lista deveria estar vazia em caso de erro");
    }
}