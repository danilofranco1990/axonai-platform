Estrutura de Pacotes do Domínio por Funcionalidade
Data: 2025-09-10

Status: Aceito

## Contexto
Durante o desenvolvimento da vertical de Identidade, surgiu a necessidade de definir um padrão consistente para a organização dos pacotes na camada de domínio (com.axonal.domain). As forças motrizes são a manutenibilidade, escalabilidade e clareza da arquitetura, que é baseada em Domain-Driven Design (DDD) e Arquitetura Hexagonal.

As alternativas consideradas foram:

Estrutura por Tipo (Package by Type): Organiza as classes com base em seu padrão técnico (ex: domain.aggregates, domain.vos, domain.services).

Estrutura por Funcionalidade (Package by Feature): Organiza as classes com base no contexto de negócio a que pertencem (ex: domain.user, domain.project).

## Decisão
Adotaremos a abordagem de Estrutura por Funcionalidade para todos os pacotes dentro da camada de domínio.

A convenção será com.axonal.domain.<contexto_de_negocio>, onde <contexto_de_negocio> representa um Bounded Context ou um Aggregate principal. Todos os objetos de domínio relacionados (Aggregates, Value Objects, Domain Services, Exceptions) para esse contexto residirão dentro deste mesmo pacote.

## Consequências
Resultados Positivos:

Alta Coesão: O código relacionado a uma única funcionalidade de negócio está fisicamente próximo, facilitando a compreensão e a manutenção.

Baixo Acoplamento: Cada pacote de funcionalidade opera como um módulo autossuficiente, tornando as dependências entre diferentes contextos de negócio mais explícitas.

Escalabilidade: A adição de novas funcionalidades é um processo limpo, consistindo na criação de um novo pacote sem impactar a estrutura existente.

Navegação Intuitiva: Desenvolvedores podem encontrar todo o código para uma tarefa em um único local.

Alinhamento com DDD: A estrutura do código reflete diretamente os Bounded Contexts, tornando a arquitetura mais expressiva.

Trade-offs, Riscos ou Limitações:

Trade-off: O número de pacotes no nível domain será maior. Este é um trade-off aceitável em troca da clareza e coesão obtidas.

Risco: Nenhum risco significativo foi identificado. A abordagem é um padrão de mercado consolidado para aplicações baseadas em DDD.