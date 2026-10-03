# Me Explique

Aplicação de acessibilidade que transforma textos presentes em imagens em conteúdo mais fácil de compreender e ouvir. O fluxo combina OCR, simplificação com inteligência artificial e conversão de texto em fala.

[![Angular](https://img.shields.io/badge/Angular-20-DD0031?logo=angular&logoColor=white)](https://angular.dev/)
[![TypeScript](https://img.shields.io/badge/TypeScript-5.8-3178C6?logo=typescript&logoColor=white)](https://www.typescriptlang.org/)
[![Java](https://img.shields.io/badge/Java-17-ED8B00?logo=openjdk&logoColor=white)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.3-6DB33F?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Tesseract OCR](https://img.shields.io/badge/Tesseract-OCR-5A5A5A)](https://github.com/tesseract-ocr/tesseract)

## O problema

Textos longos, linguagem complexa e documentos disponíveis apenas como imagem podem criar barreiras de acesso, especialmente para pessoas neurodivergentes ou com dificuldades de leitura. O Me Explique reúne esse processo em uma única interface:

```text
Imagem -> OCR -> texto original -> simplificação por IA -> leitura em voz alta
```

## Principais funcionalidades

- Envio de imagens pelo seletor de arquivos ou por arrastar e soltar.
- Correção automática da orientação da imagem por metadados EXIF.
- Pré-processamento de contraste e escala de cinza antes do OCR.
- Extração de textos em português com Tesseract/Tess4J.
- Reescrita do conteúdo em linguagem clara e direta com IA.
- Exibição lado a lado do texto original e da versão simplificada.
- Leitura em voz alta, controle de velocidade, pausa e retomada.
- Uso da Web Speech API no navegador e Google Cloud Text-to-Speech como alternativa.
- Documentação da API com Swagger/OpenAPI.

## Tecnologias

| Camada | Tecnologias |
| --- | --- |
| Frontend | Angular 20, TypeScript, SCSS, Angular Material e RxJS |
| Backend | Java 17, Spring Boot 3.5.3 e Maven |
| OCR | Tess4J / Tesseract e metadata-extractor |
| Simplificação | Together AI com o modelo DeepSeek-V3 |
| Áudio | Web Speech API e Google Cloud Text-to-Speech |
| Testes | JUnit, Mockito, Jasmine e Karma |
| Infraestrutura | Docker |

## Estrutura do projeto

```text
me-explique/
├── frontend/          # aplicação Angular
├── backend/maker/     # API Spring Boot
└── docs/              # documentação complementar
```

## Executando localmente

### Pré-requisitos

- Java 17
- Node.js 20+
- npm
- Uma chave da Together AI
- Credenciais do Google Cloud são opcionais para o TTS; sem elas, o navegador utiliza a Web Speech API

### Backend

1. Entre na pasta da API:

```bash
cd backend/maker
```

2. Configure as variáveis de ambiente:

| Variável | Obrigatória | Finalidade |
| --- | --- | --- |
| `TOGETHER_API_KEY` | Sim | Acesso ao serviço de simplificação de texto |
| `GOOGLE_APPLICATION_CREDENTIALS_BASE64` | Não | Credenciais JSON do Google Cloud codificadas em Base64 |
| `PORT` | Não | Porta HTTP; padrão: `8080` |

3. Execute a API a partir de `backend/maker`, pois o OCR utiliza a pasta `tessdata` desse diretório:

```bash
./mvnw spring-boot:run
```

No Windows:

```powershell
.\mvnw.cmd spring-boot:run
```

A API será iniciada em `http://localhost:8080`.

### Frontend

1. Em `frontend/src/app/services/api.ts`, altere `baseUrl` para a API local:

```ts
private baseUrl = 'http://localhost:8080/api';
```

2. Instale as dependências e inicie o Angular:

```bash
cd frontend
npm install
npm start
```

A interface ficará disponível em `http://localhost:4200`.

## Endpoints principais

| Método | Endpoint | Descrição |
| --- | --- | --- |
| `POST` | `/api/ocr/ler-imagem` | Extrai e simplifica o texto de uma imagem |
| `POST` | `/api/simplificar` | Simplifica um texto enviado diretamente |
| `POST` | `/api/tts/convert` | Converte texto em áudio MP3 |
| `POST` | `/api/tts/convert/custom` | Gera áudio com voz, velocidade e tom personalizados |
| `GET` | `/api/tts/status` | Informa a disponibilidade do Google Cloud TTS |
| `GET` | `/api/tts/voices` | Lista as vozes configuradas |

Com o backend em execução, a documentação interativa fica disponível em:

- Swagger UI: `http://localhost:8080/swagger-ui/index.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Testes

Backend:

```bash
cd backend/maker
./mvnw test
```

Frontend:

```bash
cd frontend
npm test
```

## Contexto

Projeto desenvolvido com foco em **acessibilidade digital** e no uso responsável de tecnologia para reduzir barreiras de compreensão. A solução foi construída em equipe durante o programa **Acelera Maker**, promovido pela Montreal.
