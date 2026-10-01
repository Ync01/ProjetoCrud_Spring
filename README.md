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

- Arquivo local em `./database/appdb` (não é versionado no Git).
- Console em http://localhost:9090/h2-console (somente no perfil `dev`, que é o padrão).
  - JDBC URL: `jdbc:h2:file:./database/appdb`
  - Usuário: `sa` / senha: vazia
- Se aparecer erro de tabela ou coluna depois de atualizar o projeto, apague a pasta `database/` e rode de novo.

## Funcionalidades

- Cadastro de usuário (senha com BCrypt, CPF validado, email único)
- Login e logout com sessão
- Entrada, edição, saída e exclusão de veículos
- Histórico de saídas e pesquisa por placa

## Rotas

| Rota | Método | Acesso |
|------|--------|--------|
| `/` | GET | público (redireciona) |
| `/login` | GET | público |
| `/autenticar` | POST | público |
| `/cadastro` | GET | público |
| `/usuarios` | POST | público |
| `/logout` | GET | público |
| `/home` | GET | login |
| `/veiculos` | GET, POST | login |
| `/veiculos/editar/{id}` | GET | login |
| `/veiculos/atualizar/{id}` | POST | login |
| `/veiculos/saida/{id}` | POST | login |
| `/veiculos/deletar/{id}` | POST | login |
