# Zelo — Ecossistema de Microsserviços

Plataforma distribuída baseada em arquitetura de microsserviços para o registro, acompanhamento e gestão de ocorrências de zeladoria urbana e cidadãos.

---

## Integrantes do Grupo

**Curso:** Sistemas de Informação — FIAP

| Nome Completo | RM |
| :--- | :---: |
| **Carlos Henrique Miranda Villarinho** | 558073 |
| **Juliana Tami Kanashiro** | 558421 |

---

## Descrição do Problema e da Solução

### O Problema
Nos centros urbanos, a comunicação entre moradores e órgãos públicos para reportar problemas de infraestrutura — como buracos em vias, falhas na iluminação pública e vazamentos de saneamento — costuma ser fragmentada e pouco transparente. Sem rastreabilidade entre quem reporta e o andamento técnico da demanda, o cidadão perde visibilidade sobre o atendimento e a gestão pública enfrenta gargalos na triagem.

### A Solução
O **Zelo** resolve esse cenário por meio de um ecossistema desacoplado de microsserviços construído com **Java 25**, **Spring Boot** e **Spring Cloud**:

* **`eureka-server` (Porta `8761`):** Servidor de descoberta de serviços (*Service Discovery*) responsável pelo registro dinâmico de todas as instâncias ativas.
* **`api-gateway` (Porta `8082`):** Ponto único de entrada da arquitetura, realizando o roteamento dinâmico e balanceamento de carga (`lb://`) para os microsserviços registrados no Eureka.
* **`ms-cidadao` (Porta dinâmica):** Gerencia o cadastro e o ciclo de status dos cidadãos (`ATIVO`, `INATIVO`, `BLOQUEADO`, `COLABORADOR_ATIVO`), expondo operações CRUD completas e endpoint para atualização síncrona de status.
* **`ms-ocorrencia` (Porta dinâmica):** Gerencia o ciclo de vida das ocorrências urbanas (`ABERTA`, `EM_ANALISE`, `EM_ANDAMENTO`, `RESOLVIDA`, `CANCELADA`). Ao registrar uma nova ocorrência, comunica-se de forma síncrona com o `ms-cidadao` via **OpenFeign** integrado ao **Resilience4j (Circuit Breaker + Fallback)** para tolerância a falhas.

---

## Tecnologias Utilizadas

* **Linguagem & Framework:** Java 25, Spring Boot 4.0.9, Spring Cloud 2025.1.4
* **Persistência & Migrações:** Spring Data JPA, Hibernate, Flyway Migrations, MySQL 8.4.6, H2 Database (perfil `test`)
* **Comunicação & Resiliência:** Spring Cloud Netflix Eureka, Spring Cloud Gateway, Spring Cloud LoadBalancer, Spring Cloud OpenFeign, Resilience4j
* **Documentação & Validação:** Springdoc OpenAPI (Swagger UI), Bean Validation, Lombok
* **Infraestrutura & Build:** Docker, Docker Compose, Maven Wrapper (`mvnw`)

---

## Arquitetura e Estrutura do Repositório

```text
Zelo_microsservice/
├── api-gateway/        # Roteamento dinâmico e entrada única (porta 8082)
├── compose/            # Configuração Docker Compose para o MySQL 8.4.6
├── eureka-server/      # Service Discovery (porta 8761)
├── ms-cidadao/         # Microsserviço de gestão de cidadãos (porta dinâmica)
├── ms-ocorrencia/      # Microsserviço de gestão de ocorrências (porta dinâmica)
└── README.md
```

---

## Como Executar a Solução (Ambiente Limpo)

### Pré-requisitos
* **Git** instalado
* **Docker** e **Docker Compose** instalados e em execução
* **JDK 25** configurado na variável de ambiente `JAVA_HOME`

*(Não é necessário ter o Maven instalado globalmente; todos os módulos possuem o **Maven Wrapper** configurado).*

---

### Passo 1: Clonar o Repositório

```bash
git clone <URL_DO_REPOSITORIO>
cd Zelo_microsservice
```

---

### Passo 2: Subir o Banco de Dados MySQL com Docker

Acesse a pasta `compose` e inicie o container do MySQL 8.4.6 (`mysql_846` na porta `3306`):

```bash
cd compose
docker compose up -d
cd ..
```

Confirme se o container está ativo:

```bash
docker ps
```

> **Nota:** Não é necessário criar os bancos de dados manualmente. Ao iniciar cada microsserviço no perfil `prod`, a conexão JDBC cria automaticamente os bancos isolados **`db_cidadao`** e **`db_ocorrencia`**, e o **Flyway** executa as migrations (`V1` e `V2`) para criar as tabelas e inserir a carga inicial de registros.

---

### Passo 3: Iniciar os Serviços na Ordem Correta

Abra **4 terminais** a partir da raiz do repositório (`Zelo_microsservice`) seguindo a ordem abaixo.

*(Nos comandos abaixo, utilize `./mvnw` no Linux/macOS/Git Bash ou `.\mvnw.cmd` no Windows PowerShell/CMD).*

1. **Eureka Server** (aguarde finalizar a inicialização na porta `8761`):
   ```bash
   cd eureka-server
   ./mvnw spring-boot:run
   ```

2. **API Gateway** (inicia na porta `8082`):
   ```bash
   cd api-gateway
   ./mvnw spring-boot:run
   ```

3. **Microsserviço de Cidadãos (`ms-cidadao`):**
   ```bash
   cd ms-cidadao
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
   ```

4. **Microsserviço de Ocorrências (`ms-ocorrencia`):**
   ```bash
   cd ms-ocorrencia
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=prod
   ```

> **Execução alternativa sem Docker (Banco H2 em memória):** Caso deseje executar os microsserviços sem subir o container MySQL, utilize `-Dspring-boot.run.profiles=test` nos passos 3 e 4.

---

## Roteiro de Testes da Solução

### 1. Verificar Registro no Eureka Server
Acesse **`http://localhost:8761`** no navegador e confirme que as três aplicações estão registradas com status **`UP`**:
* `API-GATEWAY`
* `MS-CIDADAO`
* `MS-OCORRENCIA`

---

### 2. Acessar o Swagger UI (Documentação Interativa)
Como os microsserviços utilizam porta dinâmica (`server.port=0`), localize a porta atribuída no log de inicialização de cada console (`Tomcat started on port XXXXX`) e acesse:

* **Swagger `ms-cidadao`:** `http://localhost:<PORTA_CIDADAO>/swagger-ui/index.html`
* **Swagger `ms-ocorrencia`:** `http://localhost:<PORTA_OCORRENCIA>/swagger-ui/index.html`

---

### 3. Testar Fluxo Funcional (CRUD e Comunicação Síncrona OpenFeign)

As requisições podem ser executadas diretamente pelo **Swagger UI** de cada serviço ou roteadas pelo **API Gateway** (`http://localhost:8082`):

#### A. Listar Cidadãos (Carga Inicial Flyway)
* **Requisição:** `GET http://localhost:8082/ms-cidadao/api/v1/cidadaos`
* **Resposta esperada:** `200 OK` retornando a lista de cidadãos inseridos automaticamente pela migration `V2`.

#### B. Cadastrar Novo Cidadão
* **Requisição:** `POST http://localhost:8082/ms-cidadao/api/v1/cidadaos`
* **Body (JSON):**
  ```json
  {
    "nome": "Carlos Henrique",
    "cpf": "123.456.789-00",
    "email": "carlos@email.com",
    "telefone": "11999999999"
  }
  ```
* **Resposta esperada:** `201 Created`, cabeçalho `Location` com a URI do recurso criado e corpo contendo `statusCidadao: "ATIVO"`.

#### C. Criar Ocorrência (Integração Síncrona via OpenFeign)
* **Requisição:** `POST http://localhost:8082/ms-ocorrencia/api/v1/ocorrencias`
* **Body (JSON):**
  ```json
  {
    "cidadaoId": 1,
    "titulo": "Poste com lâmpada queimada",
    "descricao": "Iluminação pública apagada em frente ao número 500.",
    "endereco": "Rua Augusta, 500 - Consolação",
    "categoria": "ILUMINACAO"
  }
  ```
* **Resposta esperada:** `201 Created`. Internamente, o `ms-ocorrencia` realiza uma chamada síncrona `PUT /api/v1/cidadaos/1/status` ao `ms-cidadao` via OpenFeign para atualizar o status do cidadão para `COLABORADOR_ATIVO`.

---

### 4. Testar Resiliência (Circuit Breaker e Fallback)

1. Encerre a execução do microsserviço **`ms-cidadao`** no terminal ou IDE.
2. Envie novamente a requisição **`POST /api/v1/ocorrencias`** para o `ms-ocorrencia`.
3. **Resultado esperado:** O cadastro da ocorrência não é interrompido, retornando **`201 Created`**, enquanto o Resilience4j aciona a classe `CidadaoFeignClientFallback` e registra no console do `ms-ocorrencia`:
   ```text
   Fallback acionado: Não foi possível atualizar o status do cidadão ID: 1
   ```
