
## Padrão de Conexão entre Camadas: Inversão de Controle e Injeção de Dependência
Data: 2025-09-11

Status: Aceito

## Contexto
A arquitetura do AxonAl é baseada nos princípios da Arquitetura Hexagonal (Ports & Adapters) , que exige uma separação rigorosa entre o núcleo da aplicação (Domínio e Casos de Uso) e a infraestrutura (banco de dados, APIs externas, etc.). A questão fundamental a ser resolvida é: "Como os componentes destas diferentes camadas devem se comunicar e ser 'conectados' uns aos outros de uma forma que preserve esse desacoplamento?".


A ausência de um padrão claro para essa conexão levaria a um código fortemente acoplado, difícil de testar e que viola os princípios arquiteturais definidos para o projeto, onde o núcleo de negócio deve permanecer puro e isolado.


## Decisão
Fica estabelecido como padrão arquitetural mandatório o uso do princípio de Inversão de Controle (IoC), implementado via Injeção de Dependência (DI) do framework Spring, para mediar toda a comunicação entre as camadas da aplicação (Adapter, Application, Domain).

Todo desenvolvedor que contribuir para o backend do AxonAl deve seguir as seguintes regras:


Definição de Portas (Interfaces): Todas as dependências externas que a Camada de Aplicação necessita (como persistência, serviços de IA, etc.) devem ser definidas como interfaces (Ports) dentro do pacote application.port.out. A camada de aplicação só pode depender de abstrações.




Implementação em Adaptadores: As implementações concretas dessas Portas devem residir na camada de adapter (ex: adapter.out.persistence, adapter.out.ai). Esses adaptadores são os únicos que podem ter conhecimento de tecnologias específicas como JPA, Spring Security, ou clientes HTTP.




Conexão via Beans do Spring: A "conexão" entre a Porta (a interface) e o Adaptador (a implementação) deve ser gerenciada pelo contêiner IoC do Spring.

Adaptadores serão declarados como Beans do Spring (ex: @Component, @Service).

Para Beans que exigem uma lógica de construção mais complexa ou configuração externa (como o PasswordEncoder), classes anotadas com @Configuration e métodos @Bean devem ser utilizados para centralizar essa lógica.

Consequências
Positivas:


Manutenção da Arquitetura Hexagonal: Este padrão é o mecanismo prático que nos força a respeitar as fronteiras do hexágono, garantindo que o núcleo da aplicação permaneça testável e agnóstico à infraestrutura.



Alta Testabilidade: Garante que qualquer serviço ou componente possa ser testado de forma isolada, pois suas dependências (as Portas) podem ser facilmente substituídas por implementações falsas ("mocks") nos testes.

Intercambialidade de Componentes: Torna o sistema "plugável". Podemos trocar uma implementação de banco de dados ou um provedor de IA alterando apenas o adaptador e a configuração, sem impactar o núcleo de negócio da aplicação.


Adesão aos Princípios SOLID: A prática reforça diretamente o Princípio da Inversão de Dependência (DIP), que é um dos pilares de um design de software robusto.

## Negativas/Trade-offs:

Dependência do Framework: A montagem da aplicação torna-se dependente do "mecanismo mágico" do contêiner de IoC do Spring. O fluxo de controle não é sempre explícito no código, mas sim gerenciado pelo framework através de anotações.

Navegabilidade do Código: Para desenvolvedores não familiarizados com o Spring, pode ser menos intuitivo rastrear qual implementação concreta de uma interface está sendo usada em tempo de execução, exigindo maior dependência de ferramentas da IDE.