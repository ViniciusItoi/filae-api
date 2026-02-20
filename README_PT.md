# 📱 Filae API - Virtual Queue Management System

## Visão Geral

**Filae** é uma API RESTful para gerenciamento de filas virtuais. O sistema permite que clientes se registrem em filas de estabelecimentos (restaurantes, barbearias, clínicas, etc.) sem precisar estar fisicamente no local, reduzindo tempo de espera e melhorando a experiência do usuário.

---

## 🚀 Quick Start

### Pré-requisitos
- Java 21+
- PostgreSQL 15+
- Maven 3.8+

### Instalação

```bash
# 1. Clone o repositório
git clone <repository-url>
cd filae-api

# 2. Configure o banco de dados
# Crie um banco de dados PostgreSQL chamado 'filae_db'
psql -U postgres -c "CREATE DATABASE filae_db;"

# 3. Compile e execute
mvn clean install
mvn spring-boot:run
```

O servidor estará disponível em `http://localhost:8080`

---

## 📚 Documentação Completa da API

### Arquivos de Documentação

1. **`API_SERVICES.txt`** - Documentação completa de todos os 26 endpoints com:
   - Descrição de cada endpoint
   - Exemplos de requisição (JSON)
   - Exemplos de resposta
   - cURLs prontos para testar
   - Informações de autenticação

2. **`Filae_API_Postman_Collection_Complete.json`** - Coleção Postman com todos os endpoints
   - Importar no Postman para testar facilmente
   - Endpoints organizados por categoria
   - Variável `{{token}}` para armazenar JWT

---

## 🔑 Autenticação

A API usa **JWT (JSON Web Token)** para autenticação.

### Fluxo de Login

```
1. POST /auth/login com email e senha
2. API retorna JWT token válido por 24 horas
3. Incluir token em Authorization header: "Bearer {token}"
4. Token automaticamente valida a sessão do usuário
```

### Headers Requeridos

```
Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJhbGljZUBleGFtcGxlLmNvbSIsImlhdCI6MTc3MTQ3ODY3NywiZXhwIjoxNzcxNTY1MDc3fQ.D_llzzettGguFWJ9D3iiBjLR5AyCoOqOi-rxd-30mP3z_82ybWJEAkZP2kxaHZd0
```

---

## 📋 Endpoints por Categoria

### 🔐 Autenticação (2 endpoints)
- `POST /api/auth/register` - Registrar novo usuário
- `POST /api/auth/login` - Login e obter JWT token

### 👤 Usuários (3 endpoints)
- `GET /api/users/me` - Obter perfil do usuário logado
- `GET /api/users/{id}` - Obter perfil de outro usuário
- `PUT /api/users/me` - Atualizar perfil do usuário

### 🏢 Estabelecimentos (4 endpoints)
- `GET /api/establishments` - Listar todos os estabelecimentos
- `GET /api/establishments/{id}` - Obter detalhes de um estabelecimento
- `POST /api/establishments` - Criar novo estabelecimento (Merchant)
- `PUT /api/establishments/{id}` - Atualizar estabelecimento (Merchant)

### 🎫 Filas (7 endpoints)
- `POST /api/queues/join` - Entrar em uma fila
- `GET /api/queues/my-queues` - Ver minhas filas
- `GET /api/queues/{id}` - Obter detalhes de uma fila
- `GET /api/queues/establishment/{id}` - Ver fila de um estabelecimento
- `PUT /api/queues/{id}/cancel` - Cancelar uma fila
- `PUT /api/queues/establishment/{id}/call-next` - Chamar próximo cliente (Merchant)
- `PUT /api/queues/{id}/finish` - Marcar fila como finalizada (Merchant)

### ⭐ Favoritos (4 endpoints)
- `POST /api/favorites` - Adicionar aos favoritos
- `GET /api/favorites` - Listar favoritos
- `DELETE /api/favorites/{id}` - Remover dos favoritos
- `GET /api/favorites/check/{id}` - Verificar se é favorito

### 🔔 Notificações (4 endpoints)
- `GET /api/notifications` - Listar notificações
- `GET /api/notifications/unread/count` - Contar não lidas
- `PUT /api/notifications/{id}/read` - Marcar como lida
- `PUT /api/notifications/read-all` - Marcar todas como lidas

### 💚 Sistema (2 endpoints)
- `GET /api/health` - Verificar se API está funcionando
- `GET /api/health/stats` - Ver estatísticas do sistema

---

## 📊 Estrutura de Dados

### Usuário
```json
{
  "id": 1,
  "name": "Alice",
  "email": "alice@example.com",
  "phone": "+55 11 98765-4321",
  "userType": "CUSTOMER",
  "profilePictureUrl": "https://example.com/alice.jpg",
  "isActive": true,
  "createdAt": "2026-02-14T01:30:00",
  "updatedAt": "2026-02-14T02:15:00"
}
```

### Estabelecimento
```json
{
  "id": 1,
  "name": "Pizzaria Bella Italia",
  "description": "Pizzaria tradicional italiana",
  "address": "Rua das Flores, 123",
  "city": "São Paulo",
  "phone": "+55 11 3333-3333",
  "category": "restaurant",
  "rating": 4.8,
  "reviewCount": 245,
  "currentWaitTime": 15,
  "estimatedServeTime": 30,
  "queueEnabled": true,
  "isAcceptingCustomers": true,
  "maxCapacity": 50,
  "currentInQueue": 8
}
```

### Fila
```json
{
  "id": 1,
  "ticketNumber": "PIZ-001",
  "establishmentId": 1,
  "establishmentName": "Pizzaria Bella Italia",
  "userId": 1,
  "userName": "Alice",
  "partySize": 4,
  "notes": "Prefer window seat",
  "position": 8,
  "totalInQueue": 12,
  "status": "WAITING",
  "estimatedWaitTime": 45,
  "joinedAt": "2026-02-14T03:00:00",
  "calledAt": null,
  "finishedAt": null
}
```

---

## 🧪 Testando a API

### Usando cURL

```bash
# 1. Login
curl -X POST 'http://localhost:8080/api/auth/login' \
  --header 'Content-Type: application/json' \
  --data '{
    "email": "alice@example.com",
    "password": "SecurePass123!"
  }'

# 2. Copiar o token da resposta
# 3. Usar em próximas requisições
curl -X GET 'http://localhost:8080/api/users/me' \
  --header 'Authorization: Bearer {seu_token_aqui}'
```

### Usando Postman

1. Importar arquivo `Filae_API_Postman_Collection_Complete.json`
2. Login primeiro para obter o token
3. Copiar o token e colar na variável `{{token}}`
4. Todos os endpoints usarão automaticamente o token

---

## 📦 Stack Tecnológico

- **Backend**: Spring Boot 3.4.1
- **Banco de Dados**: PostgreSQL 18.1
- **Autenticação**: JWT (jjwt 0.12.3)
- **ORM**: Hibernate 6.6.4
- **Validação**: Jakarta Validation 3.0.2
- **Mapeamento**: MapStruct 1.6.0
- **Build**: Maven 3.8+
- **Java**: 21+

---

## 🏗️ Estrutura do Projeto

```
filae-api/
├── src/main/java/com/filae/api/
│   ├── application/
│   │   ├── controller/         # REST Controllers
│   │   ├── dto/                # Data Transfer Objects
│   │   └── mapper/             # MapStruct Mappers
│   ├── domain/
│   │   ├── entity/             # JPA Entities
│   │   ├── repository/         # Data Access
│   │   └── service/            # Business Logic
│   └── infrastructure/
│       ├── config/             # Spring Configuration
│       ├── exception/          # Exception Handlers
│       ├── logging/            # Logging Utilities
│       └── security/           # JWT & Security
├── src/main/resources/
│   ├── application.yml         # Configuration
│   └── db/migration/           # Flyway Migrations
├── API_SERVICES.txt            # Complete API Documentation
├── Filae_API_Postman_Collection_Complete.json
└── pom.xml                     # Maven Configuration
```

---

## 🗄️ Banco de Dados

### Tabelas Principais
- `users` - Usuários do sistema
- `establishments` - Restaurantes, barbearias, etc
- `queues` - Entradas nas filas
- `favorites` - Favoritos dos usuários
- `notifications` - Notificações do sistema

### Dados Dummy para Testes

**Usuários:**
- alice@example.com / SecurePass123!
- bob@example.com / password123

**Estabelecimentos:**
- Pizzaria Bella Italia (restaurant)
- Barbearia Premium (barbershop)

---

## 🔧 Configuração

### application.yml

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/filae_db
    username: postgres
    password: we_love_security_s2
  jpa:
    hibernate:
      ddl-auto: validate
  flyway:
    enabled: true
    baselineOnMigrate: true

server:
  port: 8080
  servlet:
    context-path: /api
```

---

## 📝 Códigos de Resposta

| Código | Significado |
|--------|-------------|
| 200 | OK - Requisição bem-sucedida |
| 201 | Created - Recurso criado |
| 204 | No Content - Sucesso sem conteúdo |
| 400 | Bad Request - Requisição inválida |
| 401 | Unauthorized - Token inválido/expirado |
| 403 | Forbidden - Sem permissão |
| 404 | Not Found - Recurso não encontrado |
| 500 | Internal Server Error - Erro do servidor |

---

## 🚨 Tratamento de Erros

Toda resposta de erro segue este padrão:

```json
{
  "timestamp": "2026-02-14T02:38:49.8712014",
  "status": 400,
  "error": "Bad Request",
  "message": "Descrição do erro",
  "path": "uri=/api/endpoint",
  "validationErrors": {
    "campo": "mensagem de erro"
  }
}
```

---

## 🔐 Segurança

- ✅ Senhas com hash bcrypt
- ✅ JWT com expiração de 24 horas
- ✅ CORS configurado
- ✅ Validação de entrada (Jakarta Validation)
- ✅ SQL Injection proteção (Prepared Statements)
- ✅ HTTPS recomendado em produção

---

## 📞 Contato & Suporte

Para dúvidas ou problemas com a API, consulte o arquivo `API_SERVICES.txt` ou verifique os logs em `logs/filae-api.log`.

---

## 📄 Licença

Este projeto é fornecido sob licença privada.

---

## 🔄 Versionamento

- **Versão Atual**: 1.0.0
- **Data**: 2026-02-19
- **Status**: Em Desenvolvimento

---

**Desenvolvido com ❤️ para o projeto Filae**

