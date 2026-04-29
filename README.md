## Projeto backend - Agendamento de consultas
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white) ![Java](https://img.shields.io/badge/java-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white) ![Postman](https://img.shields.io/badge/Postman-FF6C37?style=for-the-badge&logo=postman&logoColor=white) ![MySQL](https://img.shields.io/badge/mysql-4479A1.svg?style=for-the-badge&logo=mysql&logoColor=white) ![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)  ![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)![RabbitMQ](https://img.shields.io/badge/Rabbitmq-FF6600?style=for-the-badge&logo=rabbitmq&logoColor=white)

### 📌 Descrição

Este projeto é um backend para gerenciamento e venda de ingressos, desenvolvido com foco em arquitetura escalável e desacoplada.

A aplicação simula um fluxo real de compra de ingressos, incluindo:

Criação de eventos
Reserva de ingressos (orders)
Processamento de pagamentos
Comunicação assíncrona entre serviços
Controle de concorrência e expiração de pedidos

Além disso, o sistema utiliza mensageria e cache para garantir melhor desempenho e resiliência.

 ---

### 🚀 Tecnologias

- Java 17
- Spring Boot (Web, Data JPA)
- RabbitMQ (mensageria assíncrona)
- Redis (cache e controle de expiração)
- Docker + Docker Compose
- PostgreSQL
- Postman (testes de API)

---
### ⚙️ Instalação e Execução
### ✅ Requisitos

Antes de iniciar, certifique-se de ter instalado:

- Java 17+

- Docker

- Docker Compose

### 🔧 Configuração

Clone o repositório

    git clone https://github.com/VitorHugo05/ticket.git

Entre na pasta

    cd ticket

Suba os containers

    docker-compose up --build

---

### 🌐 Serviços disponíveis

Após subir o projeto:

- API: http://localhost:8080
- RabbitMQ: http://localhost:15672
- Redis: http://localhost:8001
- Banco de dados: PostgreSQL (via Docker)

---

### 🏛️ Arquitetura

### Principais Camadas:

O projeto segue uma abordagem modular inspirada em microsserviços dentro de um monólito (modular monolith), com separação clara por domínio.

#### 📌 Domínios principais
- Events → gerenciamento de eventos
- Orders → criação e controle de pedidos
- Payments → processamento de pagamentos



#### 📌 Estrutura de Diretórios:
    
    
    └── ticket
    ├── src
    │   ├── main
    │   │   ├── java
    │   │   │   └── com
    │   │   │       └── vitordev
    │   │   │           └── ticket
    │   │   │               ├── events
    │   │   │               │   ├── controllers
    │   │   │               │   ├── messaging
    │   │   │               │   ├── model
    │   │   │               │   │   └── dto
    │   │   │               │   ├── repository
    │   │   │               │   └── services
    │   │   │               ├── orders
    │   │   │               │   ├── config
    │   │   │               │   ├── controllers
    │   │   │               │   ├── messaging
    │   │   │               │   ├── model
    │   │   │               │   │   ├── dto
    │   │   │               │   │   └── enums
    │   │   │               │   ├── repository
    │   │   │               │   └── service
    │   │   │               ├── payments
    │   │   │               │   ├── controllers
    │   │   │               │   ├── messasing
    │   │   │               │   ├── model
    │   │   │               │   │   ├── dto
    │   │   │               │   │   └── enums
    │   │   │               │   ├── repository
    │   │   │               │   └── service
    │   │   │               └── shared
    │   │   │                   ├── config
    │   │   │                   └── exceptions
    │   │   │                       └── controller
    │   │   └── resources
    │   │       ├── static
    │   │       └── templates
    │   └── test
    │       └── java
    │           └── com
    │               └── vitordev
    │                   └── ticket
    └── target
    ├── classes
    │   └── com
    │       └── vitordev
    │           └── ticket
    │               ├── events
    │               │   ├── controllers
    │               │   ├── messaging
    │               │   ├── model
    │               │   │   └── dto
    │               │   ├── repository
    │               │   └── services
    │               ├── orders
    │               │   ├── config
    │               │   ├── controllers
    │               │   ├── messaging
    │               │   ├── model
    │               │   │   ├── dto
    │               │   │   └── enums
    │               │   ├── repository
    │               │   └── service
    │               ├── payments
    │               │   ├── controllers
    │               │   ├── messasing
    │               │   ├── model
    │               │   │   ├── dto
    │               │   │   └── enums
    │               │   ├── repository
    │               │   └── service
    │               └── shared
    │                   ├── config
    │                   └── exceptions
    │                       └── controller
    └── generated-sources
    └── annotations

---

### 📦 Bibliotecas Maven

O projeto utiliza as seguintes dependências no Maven:

    <dependencies>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-amqp</artifactId>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.core</groupId>
			<artifactId>jackson-databind</artifactId>
			<version>2.17.2</version>
		</dependency>
		<dependency>
			<groupId>com.fasterxml.jackson.datatype</groupId>
			<artifactId>jackson-datatype-jsr310</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc</artifactId>
		</dependency>
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-amqp-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-webmvc-test</artifactId>
			<scope>test</scope>
		</dependency>
		<dependency>
			<groupId>redis.clients</groupId>
			<artifactId>jedis</artifactId>
			<version>7.2.0</version>
			<scope>compile</scope>
		</dependency>
	</dependencies>

---

### 📄 Modelo de Domínio

### Order

    public class OrderEntity {

        private Long id;
        private Long userId;
        private Long eventId;
        private Double price;
        private Integer quantity;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime expiresAt;
        private OrderStatus status;
    }

### Event

    public class EventEntity {
        
        private Long id;
        private String name;
        private String description;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private Double ticketPrice;
        private Integer capacity;
        private Integer sold = 0;
    }

### Paciente

    public class PaymentEntity {
        private Long id;
        private Long orderId;
        private Double amount;
        private PaymentStatus status;
        private PaymentMethods method;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
        private LocalDateTime expiresAt;
    }

---

### ❌ Tratamento de Erros

O projeto conta com um handler global de exceções, padronizando as respostas de erro para a API.
Exemplo de resposta de erro:

    {
        "timestamp": "2025-02-14T12:00:00",
        "status": 404,
        "error": "Object not found",
        "message": "Event not found",
        "path": "/api/event/1"
    }
---

### 🤝 Contribuição

Fork este repositório.

Crie uma branch com sua feature (git checkout -b minha-feature).

Commit suas mudanças (git commit -m 'Adiciona nova funcionalidade').

Envie um Pull Request.

---

### 🤝 Entre em contato

[![Email](https://img.shields.io/badge/Email-D14836?style=for-the-badge&logo=gmail&logoColor=white)](mailto:vitorhugo.pozzi@gmail.com)  [![LinkedIn](https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white)](https://www.linkedin.com/in/vitor-hugo-4a07a52ba/)  
