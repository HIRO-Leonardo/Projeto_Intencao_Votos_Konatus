# 🗳️ Sistema de Apuração de Intenção de Votos (Ponderado)

Este projeto é uma **API REST** robusta desenvolvida em Java com Spring Boot. O sistema foi projetado para processar dados de pesquisas eleitorais, integrando informações geográficas do IBGE e aplicando cálculos estatísticos de ponderação baseados em faixas populacionais.

---

## 🚀 Principais Funcionalidades

* **Sincronização com API do IBGE:** Consome dados oficiais de municípios e estados brasileiros.
* **Classificação Automática por Grupos:** Categoriza cidades em 4 grupos distintos com base na população para fins de cálculo de peso.
* **Processamento Inteligente de CSV:** Upload e leitura de planilhas com tratamento de normalização (ignora espaços extras e diferenças entre maiúsculas/minúsculas).
* **Cálculo de Votos Ponderados:** Motor de cálculo que transforma "votos brutos" em "votos ponderados" seguindo a importância demográfica de cada região.
* **Relatório de Ranking:** Endpoint de consulta que agrupa os resultados por candidato, apresentando um ranking ordenado.

---

## 📊 Regra de Negócio (Matemática da Ponderação)

Para garantir que a pesquisa reflita a realidade eleitoral, cada voto é multiplicado pelo peso do grupo ao qual o município pertence:

| Grupo | Perfil do Município | Multiplicador (Peso) |
| :--- | :--- | :--- |
| **Grupo 1** | Pequeno Porte | **1.0x** |
| **Grupo 2** | Médio Porte | **1.5x** |
| **Grupo 3** | Grande Porte / Centros Regionais | **2.0x** |
| **Grupo 4** | Metrópoles e Capitais | **2.5x** |

**Fórmula utilizada:** $Votos Ponderados = \sum (Votos Brutos \times Peso Do Grupo)$

---

## 🛠️ Tecnologias e Ferramentas

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3.x
* **Banco de Dados:** PostgreSQL
* **Persistência:** Spring Data JPA (Hibernate)
* **Segurança/Assinatura:** Suporte a Certificado Digital A1 (.pfx)
* **Gerenciador de Dependências:** Maven

---

## 📁 Estrutura do Arquivo CSV

O sistema espera um arquivo `.csv` (separado por `;`) com o seguinte layout:

| Coluna | Descrição | Exemplo |
| :--- | :--- | :--- |
| `id_pesquisa` | Identificador único da pesquisa | PESQ_2026_01 |
| `data_pesquisa`| Data da coleta (dd/MM/yyyy) | 15/03/2026 |
| `municipio` | Nome da cidade | SAO PAULO |
| `estado` | Sigla da UF | SP |
| `id_candidato` | Código numérico do candidato | 10 |
| `nome_candidato`| Nome completo do candidato | CANDIDATO A |
| `votos` | Quantidade de votos brutos | 50000 |

---

## 🛣️ Principais Endpoints

### 1. Municípios
* `GET /api/municipios/sincronizar`: Busca dados do IBGE e popula o banco de dados.

### 2. Processamento
* `POST /api/pesquisas/upload-csv`: Recebe o arquivo, valida a estrutura e salva as intenções de voto já calculadas.

### 3. Resultados
* `GET /api/pesquisas/resultados`: Retorna o JSON com o ranking final consolidado:
    ```json
    {
      "nomeCandidato": "CANDIDATO A",
      "totalVotosBrutos": 62000,
      "totalVotosPonderados": 124500.0,
      "idCandidato": 10,
      "dataPesquisa": "2026-03-15",
      "idPesquisa": "PESQ_001"
    }
    ```

---

## 🛡️ Tratamento de Exceções e Segurança

1.  **Normalização de Strings:** Implementado sistema de busca via `HashMap` que remove espaços e acentos para garantir o vínculo correto entre o CSV e o banco de dados.
2.  **Validação de Data:** Utilização de `DateTimeFormatter` customizado para evitar erros com o padrão brasileiro de datas.
3.  **Código Defensivo:** Verificações de integridade para evitar erros do tipo `NullPointerException` ou `NumberFormatException` durante a leitura de arquivos mal formatados.
4.  **Assinatura Digital:** Configuração pronta para uso de **Certificados Digitais A1** em operações de emissão que exijam validação jurídica.

---

## 💻 Como Rodar o Projeto

1. Certifique-se de ter o **PostgreSQL** rodando.
2. Clone o repositório.
3. Ajuste o `application.properties` com suas credenciais do banco.
4. Execute o comando: `mvn spring-boot:run`
5. Acesse o Swagger ou utilize Postman/Insomnia para interagir com a API.

---
Desenvolvido por **Leonardo** 🚀
