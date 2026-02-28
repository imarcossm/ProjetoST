# Sistema de Pedidos

API REST para gerenciamento de clientes, produtos e pedidos.

## Tecnologias

- Backend: Java 17, Spring Boot 3, Spring Data JPA
- Banco de Dados: MySQL 8
- Frontend: HTML5, CSS3, JavaScript
- Build: Maven

---

## Como rodar

### Opção 1 — Docker (recomendado)

> Essa é a forma mais simples. Sobe o banco e o backend com um único comando, sem precisar instalar o MySQL ou configurar credenciais na máquina.

Requisito: ter o [Docker](https://www.docker.com/) e o Docker Compose instalados.

```bash
docker-compose up --build
```

### Opção 2 — Local

Requisitos: Java 17+, MySQL 8+, Maven 3.6+

1. Crie o banco de dados executando os scripts abaixo no MySQL:

```sql
CREATE DATABASE projetost;
USE projetost;

CREATE TABLE cliente (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL,
    data_cadastro DATE NOT NULL
);

CREATE TABLE produto (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(150) NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    estoque INT NOT NULL,
    data_cadastro DATE NOT NULL
);

CREATE TABLE pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_cliente INT NOT NULL,
    data_pedido DATE NOT NULL,
    FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);

CREATE TABLE item_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_produto INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    quantidade INT NOT NULL,
    desconto DECIMAL(10,2),
    FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    FOREIGN KEY (id_produto) REFERENCES produto(id)
);
```

2. Edite o arquivo `src/main/resources/application.properties` com suas credenciais:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/projetost
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

3. Rode a aplicação:

```bash
./mvnw spring-boot:run
```

---

## Frontend

Com a aplicação no ar, basta abrir o arquivo `frontend/index.html` no navegador. A interface já está configurada para acessar a API na porta `8080`.

---

## Endpoints

### Clientes — `/clientes`

- `POST /clientes` — Cadastrar cliente
- `GET /clientes` — Listar todos (paginado)
- `GET /clientes/{id}` — Buscar por ID
- `GET /clientes/buscar?nome=X` — Buscar por nome
- `PUT /clientes/{id}` — Atualizar cliente
- `DELETE /clientes/{id}` — Remover cliente

### Produtos — `/produtos`

- `POST /produtos` — Cadastrar produto
- `GET /produtos` — Listar todos (paginado)
- `GET /produtos/{id}` — Buscar por ID
- `GET /produtos/buscar?nome=X` — Buscar por nome
- `PUT /produtos/{id}` — Atualizar produto
- `DELETE /produtos/{id}` — Remover produto

### Pedidos — `/pedidos`

- `POST /pedidos` — Criar pedido (desconta estoque automaticamente)
- `GET /pedidos` — Listar todos (paginado)
- `GET /pedidos/{id}` — Buscar por ID
- `GET /pedidos/cliente/{id}` — Pedidos de um cliente
- `GET /pedidos/produto/{id}` — Pedidos que contêm um produto
- `GET /pedidos/periodo?dataInicio=YYYY-MM-DD&dataFim=YYYY-MM-DD` — Pedidos por período
- `GET /pedidos/cliente/{id}/total` — Valor total gasto pelo cliente
- `DELETE /pedidos/{id}` — Remover pedido

---

## Exemplos de uso

Cadastrar cliente:
```json
POST /clientes
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "dataCadastro": "2026-01-15"
}
```

Cadastrar produto:
```json
POST /produtos
{
  "nome": "Notebook Dell",
  "valor": 3500.00,
  "estoque": 10,
  "dataCadastro": "2026-01-01"
}
```

Criar pedido:
```json
POST /pedidos
{
  "clienteId": 1,
  "dataPedido": "2026-02-26",
  "itens": [
    {
      "produtoId": 1,
      "valor": 3500.00,
      "quantidade": 2,
      "desconto": 100.00
    }
  ]
}
```