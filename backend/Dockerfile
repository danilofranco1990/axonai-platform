# #####################################################################
# Dockerfile para a Aplicação Backend AxonAI (Spring Boot)
# Utiliza um padrão multi-stage para uma imagem final otimizada.
# #####################################################################

# --- ESTÁGIO 1: Build (O Construtor) ---
# Usamos uma imagem base que contém o Maven e o JDK 21 para compilar nossa aplicação.
# Damos a este estágio o nome "builder" para que possamos nos referir a ele mais tarde.
FROM maven:3.9-eclipse-temurin-21 AS builder

# Define o diretório de trabalho dentro do container.
WORKDIR /app

# Copia primeiro o pom.xml para aproveitar o cache de camadas do Docker.
# Se o pom.xml não mudar, o Maven não precisará baixar as dependências novamente nas próximas builds.
COPY pom.xml .
RUN mvn dependency:go-offline

# Copia o resto do código-fonte da nossa aplicação.
COPY src ./src

# Executa o build do Maven para gerar o artefato .jar executável.
# Usamos -DskipTests porque os testes já devem ter sido validados pelo nosso pipeline de CI
# antes mesmo de chegarmos à fase de build da imagem.
RUN mvn clean package -DskipTests


# --- ESTÁGIO 2: Final (A Imagem de Produção) ---
# Começamos com uma imagem base JRE (Java Runtime Environment), que é muito menor
# do que a imagem JDK, pois não contém as ferramentas de compilação.
# A versão "alpine" é uma distribuição Linux mínima, o que a torna ainda menor e mais segura.
FROM eclipse-temurin:21-jre-alpine

# Define o diretório de trabalho.
WORKDIR /app

# Copia APENAS o .jar compilado do estágio "builder" para a nossa imagem final.
# Nenhum código-fonte ou ferramenta de build (Maven, JDK completo) é incluído aqui.
COPY --from=builder /app/target/*.jar app.jar

# Expõe a porta em que a aplicação Spring Boot roda por padrão para a rede do Docker.
EXPOSE 8080
COPY entrypoint.sh .
RUN chmod +x ./entrypoint.sh
# O comando que será executado quando o container iniciar, efetivamente rodando nossa aplicação.
ENTRYPOINT ["./entrypoint.sh"]
