## 5. Estrutura de Pacotes por Bounded Context (Package-by-Context)
Data: 2025-09-12

Status: Aceito

## Contexto
À medida que o sistema AxonAl cresce e incorpora novas capacidades de negócio (Identidade, Projetos, Faturamento, etc.), a organização do código-fonte torna-se um fator crítico para a manutenibilidade e escalabilidade. Uma estrutura de pacotes que não reflete os limites do negócio pode levar a um alto acoplamento e baixa coesão, resultando em um "Monólito Big Ball of Mud".

A decisão que precisa ser tomada é como estruturar os pacotes para que a arquitetura do código seja um reflexo direto do nosso design estratégico (DDD), suportando o objetivo de construir um 

Monólito Modular.

A alternativa considerada foi a estrutura "Package-by-Layer", onde os pacotes de nível superior seriam application, domain, e adapter, misturando código de todos os diferentes contextos de negócio.

## Decisão
Fica definida a adoção da estratégia "Package-by-Context" como o padrão mandatório para a organização do código-fonte do backend.

Pacotes de Nível Superior = Bounded Contexts: Cada subdomínio de negócio principal (ex: identity, project) terá seu próprio pacote de nível superior. Todo o código relacionado a essa capacidade de negócio residirá dentro deste pacote.

Estrutura Hexagonal Interna: Dentro de cada pacote de contexto, a estrutura de camadas da Arquitetura Hexagonal (application, domain, adapter) deve ser replicada. Isso cria uma "mini-aplicação" coesa e bem-estruturada para cada contexto.

Núcleo Compartilhado (Shared Kernel): Para gerenciar o acoplamento controlado entre os contextos, um pacote de nível superior shared_kernel será utilizado. Ele conterá uma pequena e estável porção do modelo de domínio (primariamente Value Objects, como UserId) que precisa ser referenciada por mais de um contexto.

A estrutura resultante será a seguinte:

com.axonai.platform
├── identity                  // <-- Bounded Context de Identidade
│   ├── application
│   ├── domain
│   └── adapter
│
├── project                   // <-- Futuro Bounded Context de Projeto
│   ├── application
│   ├── domain
│   └── adapter
│
└── shared_kernel             // <-- Código compartilhado entre contextos
    └── domain
        └── model
## Consequências
Positivas:

Alta Coesão: Todo o código relacionado a uma única capacidade de negócio está agrupado, tornando o sistema mais fácil de entender e modificar.

Limites de Contexto Explícitos: A estrutura de pacotes torna os limites dos Bounded Contexts do DDD explícitos, servindo como uma forma de auto-documentação da arquitetura.

Fundação para Monólito Modular: Esta é a base para um monólito saudável que pode crescer de forma organizada e, se necessário, ter seus módulos extraídos para microserviços com esforço drasticamente reduzido.

Redução da Carga Cognitiva: Os desenvolvedores podem focar em um único contexto de cada vez, sem precisar navegar por todo o código-fonte para completar uma tarefa.

Negativas/Trade-offs:

Estrutura de Pacotes Mais Profunda: Os nomes dos pacotes se tornam mais longos (ex: com.axonai.platform.identity.application.port.in), e a navegação pode parecer mais aninhada.

Disciplina do Shared Kernel: Exige disciplina rigorosa da equipe para manter o shared_kernel o menor e mais estável possível. Se ele crescer descontroladamente, pode se tornar uma fonte de alto acoplamento entre os contextos, minando os benefícios da modularidade.