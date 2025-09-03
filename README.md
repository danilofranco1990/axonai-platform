# AxonAI Platform

![CI Status](https://img.shields.io/badge/CI-Pending-yellow)
![Code Coverage](https://img.shields.io/badge/Coverage-Pending-yellow)
![License](https://img.shields.io/badge/License-MIT-blue)

## 🎯 Visão Geral

O AxonAI é um orquestrador inteligente projetado para resolver a ineficiência e a perda de contexto no desenvolvimento de software assistido por IA. O fluxo de trabalho atual entre conversar com um LLM e executar as tarefas em ferramentas de projeto é manual e desarticulado. AxonAI transforma essas conversas em planos de projeto executáveis e estruturados (tarefas e checklists).

A funcionalidade chave é o **"Modo Foco"**, que cria um chat contextualizado para cada item de checklist, fornecendo à IA apenas o histórico relevante para aquela micro-tarefa, aumentando drasticamente a precisão da IA e a produtividade do desenvolvedor.

## 🛠️ Stack Tecnológica

| Camada          | Tecnologias Principais                                                                   |
| --------------- | ---------------------------------------------------------------------------------------- |
| **Backend** | Java 21 LTS, Spring Boot 3, Maven                        |
| **Frontend** | React 19, Vite, TypeScript                                         |
| **Banco de Dados** | PostgreSQL                                                                    |
| **Infraestrutura** | PaaS via [Render.com](https://render.com), Docker & Docker Compose (para dev local) |

## 🚀 Começando (Getting Started)

Estas instruções permitirão que você tenha uma cópia do projeto rodando na sua máquina local para propósitos de desenvolvimento e teste.

### Pré-requisitos

Você precisa ter as seguintes ferramentas instaladas:

* Git
* JDK 21 LTS
* Node.js (versão 20 ou superior)
* Docker e Docker Compose

### Configuração e Execução

O projeto é projetado para ser executado com um único comando, graças ao Docker Compose, que orquestra o backend e o banco de dados.

1.  **Clone o repositório:**
    ```bash
    git clone [URL_DO_SEU_REPOSITORIO]
    cd axonal-platform
    ```

2.  **Suba o ambiente completo (Backend + Banco de Dados):**
    ```bash
    docker-compose up --build
    ```
    * O comando `--build` é necessário na primeira vez para construir a imagem Docker do backend.

3.  **Execute o Frontend:**
    * Em um terminal separado, navegue até a pasta do frontend e inicie o servidor de desenvolvimento:
    ```bash
    cd frontend 
    npm install
    npm run dev
    ```

Após estes passos, o ambiente estará totalmente funcional:

* **API do Backend** estará disponível em `http://localhost:8080`
* **Documentação da API (Swagger UI)** estará em `http://localhost:8080/swagger-ui.html`
* **Aplicação Frontend** estará disponível em `http://localhost:5173`

## 🏛️ Arquitetura

O sistema é um **Monolito Modular** que segue os princípios da **Arquitetura Hexagonal (Ports & Adapters)** e do **Domain-Driven Design (DDD)**.

A estrutura de pacotes do backend reflete esta escolha, com uma separação clara entre:
* `domain`: O núcleo de negócio puro.
* `application`: A camada de orquestração (casos de uso).
* `adapter`: As implementações de infraestrutura (web, persistência, etc.).

## 🤝 Contribuição

Todo o desenvolvimento segue um fluxo de trabalho baseado em *feature branches* e Pull Requests. Por favor, consulte o futuro arquivo `CONTRIBUTING.md` para mais detalhes sobre as convenções de código e o processo de contribuição.
