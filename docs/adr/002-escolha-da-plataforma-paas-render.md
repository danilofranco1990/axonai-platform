# 2. Escolha da Plataforma de Nuvem (PaaS com Render)

* **Data:** 2025-09-04
* **Status:** Aceito

## Contexto

O projeto AxonAI necessita de uma plataforma de hospedagem para seus ambientes de Staging e Produção. Os requisitos principais para esta plataforma são:
1.  Simplificar a complexidade de infraestrutura para que a equipe possa focar no desenvolvimento do produto.
2.  Integrar-se de forma nativa com um fluxo de trabalho baseado em Git ("GitOps").
3.  Oferecer gestão integrada de banco de dados e de segredos (variáveis de ambiente).
4.  Permitir escalabilidade simplificada conforme o crescimento do produto.
5.  Manter os custos operacionais iniciais previsíveis e baixos.

A alternativa principal considerada foi o uso de IaaS (Infrastructure as a Service), como AWS EC2 ou Google Compute Engine.

## Decisão

Foi decidido adotar uma estratégia de **Platform as a Service (PaaS)** para a hospedagem da aplicação. A plataforma escolhida para implementação inicial é o **Render.com**.

O Render será utilizado para provisionar os seguintes serviços:
* Um "Web Service" para executar o container Docker da nossa aplicação backend Spring Boot.
* Um "Private Service" para hospedar nosso banco de dados PostgreSQL.

A implantação será automatizada via "Deploy Hooks" acionados pelo nosso pipeline de CI/CD no GitHub Actions, seguindo um modelo "Git push-to-deploy".

## Consequências

* **Positivas:**
    * **Foco no Produto:** A equipe de desenvolvimento pode focar 100% na codificação do AxonAI, pois o Render abstrai a complexidade de gerenciamento de servidores, redes, sistemas operacionais e backups de banco de dados.
    * **Velocidade de Implantação:** O modelo "push-to-deploy" permite que novas versões da aplicação sejam implantadas em Staging e Produção de forma rápida e automatizada, acelerando o ciclo de feedback e a entrega de valor.
    * **Redução da Carga Operacional:** A responsabilidade pela manutenção e segurança da infraestrutura subjacente é delegada ao provedor do PaaS.
    * **Ambiente Integrado:** O Render oferece uma gestão unificada de todos os componentes da nossa aplicação (backend, banco de dados, segredos), simplificando a configuração.

* **Negativas/Trade-offs:**
    * **Menor Controle Granular:** Em comparação com uma solução IaaS, temos menos controle sobre a configuração fina da máquina virtual, da rede ou do sistema operacional. Aceitamos este trade-off em favor da simplicidade.
    * **Potencial "Vendor Lock-in":** Ao usar funcionalidades específicas do Render (como os "Blueprints"), criamos uma dependência da plataforma. No entanto, como nossa aplicação é containerizada com Docker, a portabilidade para outro provedor que suporte containers é relativamente alta.
    * **Modelo de Custo:** Em uma escala massiva, o custo de um PaaS pode se tornar maior do que o de uma infraestrutura IaaS otimizada. Para o estágio atual do projeto, o modelo de custo do PaaS é mais vantajoso.