# Use Java 17
FROM openjdk:17-jdk-slim

# Instalar dependências do Tesseract
RUN apt-get update && apt-get install -y \
    tesseract-ocr \
    tesseract-ocr-por \
    wget \
    && rm -rf /var/lib/apt/lists/*

# Criar diretório de trabalho
WORKDIR /app

# Copiar arquivos Maven
COPY pom.xml .
COPY mvnw .
COPY mvnw.cmd .
COPY .mvn .mvn
COPY tessdata /app/tessdata

# Dar permissão para mvnw
RUN chmod +x mvnw

# Baixar dependências
RUN ./mvnw dependency:go-offline -B

# Copiar código fonte
COPY src src

# Construir aplicação
RUN ./mvnw clean package -DskipTests

# Expor porta
EXPOSE $PORT

# Comando para executar
CMD ["sh", "-c", "java -Dserver.port=$PORT -jar target/maker-simplificador-0.0.1-SNAPSHOT.jar"]