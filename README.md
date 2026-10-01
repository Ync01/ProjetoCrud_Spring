# ParkFlow

Sistema escolar de controle de estacionamento: registra a entrada e a saída de veículos e mantém o histórico.

## Tecnologias

Java 21, Spring Boot, Spring Data JPA, Thymeleaf, H2 (arquivo), Maven, BCrypt (spring-security-crypto).

## Como executar

```
mvnw spring-boot:run
```

(no Linux/Mac: `./mvnw spring-boot:run`)

A aplicação sobe na porta **9090**: http://localhost:9090

## Banco H2

- Arquivo local em `./database/appdb`.
- O console do H2 fica **desligado** por padrão (dá acesso total ao banco). Para usar em desenvolvimento:
  `./mvnw spring-boot:run -Dspring-boot.run.profiles=dev` e abra http://localhost:9090/h2-console
  - JDBC URL: `jdbc:h2:file:./database/appdb`
  - Usuário: `sa` / senha: vazia
- Ao iniciar, a aplicação ajusta automaticamente bancos antigos sem apagar dados: cria a coluna `tipo`
  (usuários antigos viram `USER`) e converte senhas em texto puro para BCrypt.

## Perfis e permissões

| Ação | ADMIN | USER |
|------|:-----:|:----:|
| Login | Sim | Sim |
| Visualizar veículos / pesquisar / histórico | Sim | Sim |
| Registrar entrada, editar, excluir, dar saída | Sim | Não |
| Gerenciar usuários (`/admin/usuarios`) | Sim | Não |

- O cadastro público (`/cadastro`) sempre cria `USER`.
- Se o banco não tiver nenhum ADMIN, a aplicação cria um ao iniciar:
  `admin@parkflow.com` / `admin1234` (pode ser alterado pelas variáveis `PARKFLOW_ADMIN_EMAIL`,
  `PARKFLOW_ADMIN_SENHA` e `PARKFLOW_ADMIN_NOME`). **Troque essa senha após o primeiro login.**
- As permissões são conferidas no backend (`AutenticacaoInterceptor` e `AdminInterceptor`);
  esconder botões no Thymeleaf é só visual.

## Funcionalidades

- Cadastro de usuário (senha com BCrypt, CPF validado, email e CPF únicos)
- Login e logout com sessão, proteção CSRF em todos os formulários POST
- Entrada, edição, saída e exclusão de veículos (ADMIN)
- Histórico de saídas e pesquisa por placa (ADMIN e USER)
- Gerenciamento de usuários e perfis (ADMIN)

## Rotas

| Rota | Método | Acesso |
|------|--------|--------|
| `/` | GET | público (redireciona) |
| `/login` | GET | público |
| `/autenticar` | POST | público |
| `/cadastro` | GET | público |
| `/usuarios` | POST | público (cria sempre USER) |
| `/logout` | POST | público |
| `/home` | GET | login |
| `/veiculos` | GET | login (ADMIN e USER), aceita `?placa=` |
| `/veiculos` | POST | ADMIN |
| `/veiculos/editar/{id}` | GET | ADMIN |
| `/veiculos/atualizar/{id}` | POST | ADMIN |
| `/veiculos/saida/{id}` | POST | ADMIN |
| `/veiculos/deletar/{id}` | POST | ADMIN |
| `/admin/usuarios` | GET, POST | ADMIN |
| `/admin/usuarios/editar/{id}` | GET | ADMIN |
| `/admin/usuarios/atualizar/{id}` | POST | ADMIN |
| `/admin/usuarios/deletar/{id}` | POST | ADMIN |

## Testes

```
./mvnw test
```

Os testes usam um banco H2 em memória (`src/test/resources/config/application.properties`), então não alteram `database/appdb`.
