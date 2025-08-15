# 📦 Order Tracking System

A microservices-based real-time order tracking platform built using **Spring Boot**, **Apache Kafka**, **Redis** and **Docker**.

---

## ✅ Features

- **Microservices Architecture** – Orders, Tracking and Notification implemented as decoupled services  
- **Event Driven** – `OrderCreatedEvent` and `OrderUpdatedEvent` are published over Kafka  
- **Redis Caching with TTL** – Tracking service stores latest order state in Redis and automatically expires stale entries  
- **Mock Notifications** – Notification service listens to Kafka and logs a mock SMS / Email  
- **Type-Safe Deserialization** – `ErrorHandlingDeserializer` + custom consumer factories per event type  
- **Prometheus + Grafana Monitoring**  
- **Unit Tests (JUnit5 + Mockito)** – create / fetch / update flows covered

---

## 🛠 Tech Stack

| Component    | Technology                      |
|--------------|----------------------------------|
| Language     | Java 17                          |
| Framework    | Spring Boot 3.5                  |
| Messaging    | Apache Kafka                     |
| Cache        | Redis                            |
| Persistence  | PostgreSQL                       |
| Monitoring   | Prometheus + Grafana             |
| Build Tool   | Maven                            |
| Container    | Docker + Docker Compose          |
| Testing      | JUnit 5 + Mockito                |

---

## 📂 Microservices

| Service                 | Description                                                                 |
|------------------------|------------------------------------------------------------------------------|
| `orders-service`        | REST API to create & update orders, publishes Kafka events                   |
| `tracking-service`      | Consumes Kafka events and stores them in Redis, exposes `/track/{id}`       |
| `notification-service`  | Consumes Kafka events and logs a mock notification                          |

---

## 🧪 Example API Calls

```bash
# Create Order (orders-service)
curl -X POST http://localhost:8081/orders/createOrder \
  -H "Content-Type: application/json" \
  -d '{"customerName": "Vikrant","item": "Book","quantity": 1}'

# Update Order Status
curl -X PUT http://localhost:8081/orders/updateOrderStatus/11?status=SHIPPED

# Track Order (tracking-service)
curl http://localhost:8082/track/11
```

---

## 🚀 Running the Project

```bash
# Start Kafka, Redis, Postgres, Prometheus and Grafana
docker compose up -d

# Start each Spring Boot service
cd orders-service && ./mvnw spring-boot:run
cd tracking-service && ./mvnw spring-boot:run
cd notification-service && ./mvnw spring-boot:run
```

---

## 📊 Monitoring

| Tool       | URL                      |
|------------|--------------------------|
| Prometheus | http://localhost:9090    |
| Grafana    | http://localhost:3000    |

> Login → `admin / admin`  
> Add `Prometheus (http://prometheus:9090)` as a data source  
> Import dashboard ID **4701** to visualize JVM / HTTP / Redis / Kafka metrics

---

## 📈 Metrics You Can Query

| Metric Name                                  | Description                                     |
|----------------------------------------------|-------------------------------------------------|
| `http_server_requests_seconds_count`         | REST endpoint usage                             |
| `kafka_consumer_records_consumed_total`      | Kafka consumer throughput                       |
| `redis_commands_processed_total`             | Redis operations                                |
| `jvm_memory_used_bytes`                      | JVM memory usage                                |

> Use the *Explore* tab in Grafana to run PromQL queries

---

## ✅ Tested Functionality

| Test Type             | Covered Scenario                   |
|----------------------|------------------------------------|
| Unit Test – Create   | Order creation + Kafka publish     |
| Unit Test – Fetch    | Successful fetch + Not-Found case  |
| Unit Test – Update   | Status update + exception path     |

---

## 🔮 What Can Be Added Next

- Kafka **dead-letter topic** for failed events  
- Swagger / OpenAPI (auto generate API docs)  
- Spring Cloud Gateway as API gateway  
- Controller test using `MockMvc`

---

👨‍💻 *Author*: **Vikrant Goyal**
