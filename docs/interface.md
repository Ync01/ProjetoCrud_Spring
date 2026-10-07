# Interface do ParkFlow

A interface usa os templates Thymeleaf existentes. Java, rotas, perfis,
formulários e tokens CSRF foram preservados.

## Arquivos

- `src/main/resources/static/css/style.css`: cores, tipografia, layouts e responsividade.
- `src/main/resources/static/js/interface.js`: perspectiva por rolagem e visibilidade da senha.
- `src/main/resources/static/js/veiculos.js`: abas por teclado, pesquisa local e formatação da placa.
- `src/main/resources/static/img/parking-plan.svg`: ilustração vetorial editável.
- `src/main/resources/static/apresentacao.html`: apresentação pública em `/apresentacao.html`.

A rota `/` continua seguindo o redirecionamento existente. Login e início
possuem links para a apresentação. A prévia da apresentação usa dados fictícios,
identificados como ilustrativos; ela não consulta o banco.

## Referências de movimento

Implementações próprias em CSS e JavaScript, inspiradas nas referências fornecidas:

- [Container Scroll Animation](https://21st.dev/@manuarora700/components/container-scroll-animation): perspectiva do painel na apresentação.
- [Liquid Metal Button](https://21st.dev/@johuniq/components/liquid-metal-button): superfície metálica e reflexo ao passar o cursor, usando CSS em vez do shader original.
- [Animated hero](https://21st.dev/@tommyjepsen/components/animated-hero): inspiração para a entrada sequencial das linhas do título.

Não há React, Tailwind, shaders, fontes remotas ou dependências adicionais.
As animações respeitam `prefers-reduced-motion`. A perspectiva fica desativada
em telas pequenas. Tabelas têm rolagem própria; os cabeçalhos, campos e controles
possuem identificação acessível e foco visível.

## Validação

Executar os testes existentes com H2 em memória. Para revisão visual,
iniciar uma instância em porta separada com
`--spring.datasource.url=jdbc:h2:mem:parkflow_preview` e
`--spring.jpa.hibernate.ddl-auto=create-drop`.
Não usar o banco em arquivo para testes que alteram registros.
