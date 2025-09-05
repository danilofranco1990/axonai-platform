# AxonAI Platform

![CI Status](https://img.shields.io/badge/CI-Pending-yellow)
[![codecov](https://codecov.io/github/danilofranco1990/axonai-platform/graph/badge.svg?token=L1CTD2VTGM)](https://codecov.io/github/danilofranco1990/axonai-platform)
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

Com Docker e Docker Compose instalados, o ambiente completo (Backend + Banco de Dados) pode ser iniciado com um único comando a partir da raiz do projeto.

1.  **Clone o repositório (se ainda não o fez):**
    ```bash
    git clone [URL_DO_SEU_REPOSITORIO]
    cd axonal-platform
    ```

2.  **Crie seu arquivo `.env` local:**
    * O backend precisa de um arquivo `.env` com as credenciais do banco de dados para rodar. Nós fornecemos um template para isso.
    ```bash
    # A partir da raiz do projeto
    cd backend
    cp .env.example .env
    # Edite o arquivo .env se precisar alterar os valores padrão
    cd ..
    ```

3.  **Suba todo o ambiente com Docker Compose:**
    ```bash
    docker-compose up --build
    ```
    * O comando `--build` é recomendado na primeira vez ou sempre que o `Dockerfile` ou o código-fonte do backend for alterado. Para apenas iniciar o ambiente, `docker-compose up` é suficiente.

4.  **Execute o Frontend (em um terminal separado):**
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
