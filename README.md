- Sistema de Pedidos

API REST para gerenciamento de clientes, produtos e pedidos, desenvolvida como desafio técnico.

- Tecnologias

Backend: Java 17, Spring Boot 3, Spring Data JPA
Banco de Dados: MySQL
Frontend: HTML5, CSS3, JavaScript puro
Build: Maven

- Pré-requisitos

- Java 17+
- MySQL 8+
- Maven 3.6+ (ou usar o wrapper incluso)

- Configuração - Banco de dados

Execute os scripts abaixo no seu MySQL para criar o banco e as tabelas:

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

### Credenciais

Edite `src/main/resources/application.properties` com as suas credenciais:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/projetost
spring.datasource.username=SEU_USUARIO
spring.datasource.password=SUA_SENHA
```

## Como Rodar o Projeto (Recomendado)

Para facilitar a avaliação técnica, a aplicação foi dockerizada. Com isso, banco de dados e backend sobem com um único comando, sem precisar instalar MySQL ou configurar credenciais localmente na sua máquina.

### Pré-requisitos para rodar via Docker:
- [Docker](https://www.docker.com/) e [Docker Compose](https://docs.docker.com/compose/) instalados.

### Passos:
1. Abra o terminal na raiz do projeto.
2. Execute o comando:
```bash
docker-compose up --build
```
Isso irá construir a imagem do backend e subir o contêiner do MySQL. O primeiro start pode demorar alguns minutos para baixar a imagem do MySQL e compilar o app via Maven.

A API estará disponível em `http://localhost:8080`.

**Frontend**

Com os contêineres rodando, basta dar um duplo clique no arquivo `frontend/index.html` para abri-lo no seu navegador favorito. A interface está configurada para acessar a API na porta `8080`.

## Endpoints

### Clientes `/clientes`

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/clientes` | Cadastrar cliente |
| GET | `/clientes` | Listar todos (paginado) |
| GET | `/clientes/{id}` | Buscar por ID |
| GET | `/clientes/buscar?nome=X` | Buscar por nome |
| PUT | `/clientes/{id}` | Atualizar cliente |
| DELETE | `/clientes/{id}` | Remover cliente |

### Produtos `/produtos`

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/produtos` | Cadastrar produto |
| GET | `/produtos` | Listar todos (paginado) |
| GET | `/produtos/{id}` | Buscar por ID |
| GET | `/produtos/buscar?nome=X` | Buscar por nome |
| PUT | `/produtos/{id}` | Atualizar produto |
| DELETE | `/produtos/{id}` | Remover produto |

### Pedidos `/pedidos`

| Método | Endpoint | Descrição |
|---|---|---|
| POST | `/pedidos` | Criar pedido (desconta estoque automaticamente) |
| GET | `/pedidos` | Listar todos (paginado) |
| GET | `/pedidos/{id}` | Buscar por ID |
| GET | `/pedidos/cliente/{id}` | Pedidos de um cliente |
| GET | `/pedidos/produto/{id}` | Pedidos que contêm um produto |
| GET | `/pedidos/periodo?dataInicio=YYYY-MM-DD&dataFim=YYYY-MM-DD` | Pedidos por período |
| GET | `/pedidos/cliente/{id}/total` | Valor total gasto pelo cliente |
| DELETE | `/pedidos/{id}` | Remover pedido |

## Exemplos de uso

**Cadastrar cliente**
```json
POST /clientes
{
  "nome": "João Silva",
  "email": "joao@email.com",
  "dataCadastro": "2026-01-15"
}
```

**Cadastrar produto**
```json
POST /produtos
{
  "nome": "Notebook Dell",
  "valor": 3500.00,
  "estoque": 10,
  "dataCadastro": "2026-01-01"
}
```

**Criar pedido**
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