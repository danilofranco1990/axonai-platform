# 1. Escolha da Stack Tecnológica Principal

* **Data:** 2025-09-04
* **Status:** Aceito

## Contexto

Para o desenvolvimento do projeto AxonAI, é necessário definir uma stack tecnológica coesa, moderna e produtiva para o backend e o frontend, que suporte os princípios de Arquitetura Limpa e Domain-Driven Design. As tecnologias escolhidas devem ter um ecossistema robusto e serem adequadas para a construção de uma aplicação web de produção.

## Decisão

Foi decidido adotar a seguinte stack principal:
* **Backend:** Java 21 LTS com o framework Spring Boot 3.
* **Frontend:** React 19 com a ferramenta de build Vite, utilizando TypeScript.
* **Banco de Dados:** PostgreSQL.
* **Gerenciamento de Build:** Maven para o backend, NPM para o frontend.

## Consequências

* **Positivas:**
    * O ecossistema Spring Boot oferece uma vasta gama de ferramentas para segurança, persistência (JPA) e criação de APIs REST, acelerando o desenvolvimento.
    * Java é uma plataforma madura, performática e robusta para o backend.
    * React é a biblioteca líder de mercado para UIs, com uma enorme comunidade e ecossistema.
    * Vite proporciona uma experiência de desenvolvimento frontend extremamente rápida.
    * TypeScript adiciona segurança de tipos, reduzindo bugs em tempo de execução.
* **Negativas/Trade-offs:**
    * A combinação resulta em dois ecossistemas distintos (Java e Node.js) para manter e construir, o que exige familiaridade com ambos.
    * O tempo de inicialização do Spring Boot, embora rápido, é maior que o de outras plataformas como Node.js (Express) ou Go.