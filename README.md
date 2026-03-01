- **Tecnologias Utilizadas**

Backend: Java 21, Spring Boot 3.4.3
Persistencia: Spring Data JPA com Native Query
Banco de Dados: MySQL 8.0
Frontend: HTML5, CSS3 e JavaScript Puro (Vanilla JS)
Containerizacao: Docker e Docker Compose
Build Tool: Maven

- **Como Executar o Projeto**

Opção 1 — Docker (Recomendado)

Esta opção sobe automaticamente o banco de dados MySQL e a aplicação Java.

1- Certifique-se de ter o **Docker Desktop** instalado e rodando.
2- Na raiz do projeto, execute:

docker compose up -d --build

3- Aguarde a inicialização. A aplicação estará disponível em `http://localhost:8080`.
4- Abra o arquivo `frontend/index.html` no seu navegador.

Opção 2 — Execução Local

1- Banco de Dados: Crie um schema chamado projetost no seu MySQL local.
2- Scripts SQL: Execute o script de criacao de tabelas abaixo (o Spring tambem pode criar automaticamente via ddl-auto=update no application.properties).
3- Configuracao: Ajuste as credenciais no arquivo src/main/resources/application.properties se necessario.
4- Rodar App:

./mvnw spring-boot:run

5- Abra o frontend/index.html no navegador.

---

- **Scripts de Criação (DDR)**

Caso deseje criar as tabelas manualmente:

CREATE DATABASE IF NOT EXISTS projetost;
USE projetost;

CREATE DATABASE IF NOT EXISTS projetost;
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
    CONSTRAINT fk_cliente FOREIGN KEY (id_cliente) REFERENCES cliente(id)
);

CREATE TABLE item_pedido (
    id INT AUTO_INCREMENT PRIMARY KEY,
    id_pedido INT NOT NULL,
    id_produto INT NOT NULL,
    valor DECIMAL(10,2) NOT NULL,
    quantidade INT NOT NULL,
    desconto DECIMAL(10,2),
    CONSTRAINT fk_pedido FOREIGN KEY (id_pedido) REFERENCES pedido(id),
    CONSTRAINT fk_produto FOREIGN KEY (id_produto) REFERENCES produto(id)
);

- **Decisões Técnicas e Justificativas**

1- Arquitetura: Utilizado o padrao MVC (Model-View-Controller) com camada de Service para isolar as regras de negocio.
2- DTOs (Java Records): Utilizei Java Records para os DTOs por serem imutaveis, concisos e ideais para transferencia de dados, aproveitando os recursos do Java 21.
3- Consultas Nativas (Native Queries): Conforme exigido pelo desafio, as principais logicas de busca e relatorios nos Repositories utilizam @Query(nativeQuery = true), demonstrando dominio em manipulacao direta de SQL.
4- Gestao de Estoque: A atualizacao do estoque e feita na camada de PedidoService dentro de uma transacao (@Transactional). Se houver falha em qualquer item do pedido ou estoque insuficiente, a operacao sofre rollback completo, garantindo integridade.
5- Frontend Vanilla JS: Optei por nao usar frameworks pesados como React ou Angular para manter a simplicidade, focando em uma UI moderna e responsiva usando CSS puro.
6- Tratamento de Erros: Implementado um GlobalExceptionHandler para retornar respostas padronizadas em JSON com mensagens claras para o usuario final.

- **Endpoints Principais**

GET /clientes - Listar e filtrar por nome.
GET /produtos - Listar e consultar estoque.
POST /pedidos - Criar pedido completo com multiplos itens.
GET /pedidos/cliente/{id}/total - Consulta de valor acumulado por cliente.
GET /pedidos/periodo - Filtro de pedidos por intervalo de datas.