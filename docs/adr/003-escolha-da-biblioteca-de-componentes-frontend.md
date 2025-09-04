# 3. Escolha da Coleção de Componentes do Frontend

* **Data:** 2025-09-04
* **Status:** Aceito

## Contexto

Para acelerar o desenvolvimento da interface do usuário (UI) do AxonAI, é necessária a adoção de uma biblioteca ou coleção de componentes que forneça blocos de construção de alta qualidade, consistentes e acessíveis. A escolha deve priorizar a customização, a manutenibilidade a longo prazo e evitar um forte acoplamento (vendor lock-in) com um único fornecedor de UI. As duas principais alternativas consideradas foram a biblioteca de componentes Mantine e a coleção de componentes Shadcn/ui.

## Decisão

Foi decidido adotar o **Shadcn/ui** como nossa coleção de componentes base para o frontend.

Esta não é uma biblioteca de dependências tradicional. A abordagem consiste em usar a CLI do Shadcn/ui para copiar o código-fonte de componentes individuais e reutilizáveis para dentro do nosso próprio repositório, no diretório `src/components/ui`. Cada componente é construído sobre as primitivas de UI acessíveis do Radix UI e estilizado com Tailwind CSS.

## Consequências

* **Positivas:**
    * **Propriedade Total do Código:** O código dos componentes reside em nosso repositório. Temos 100% de controle para modificar, adaptar ou estender cada componente conforme a necessidade do nosso design, sem depender das abstrações de uma biblioteca externa.
    * **Customização Ilimitada:** Como somos donos do código, não há limites para a customização.
    * **Sem Acoplamento (No Lock-in):** A aplicação não depende de uma "biblioteca de UI". Se o Shadcn/ui deixar de ser mantido, nosso projeto não é afetado, pois já temos o código.
    * **Acessibilidade Sólida:** Os componentes são construídos sobre o Radix UI, que é um padrão da indústria para a criação de componentes de UI acessíveis.
    * **Leveza:** Apenas o código dos componentes que efetivamente usamos é incluído no nosso projeto.

* **Negativas/Trade-offs:**
    * **Maior Responsabilidade:** Como o código é nosso, somos responsáveis por sua manutenção.
    * **Curva de Aprendizagem:** A abordagem pressupõe conforto com o ecossistema React e, principalmente, com o Tailwind CSS para estilização.
    * **Processo de Adição:** Adicionar um novo componente requer um passo explícito via CLI, em vez de um simples `import` de uma biblioteca já instalada.