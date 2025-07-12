# 📦 Order Tracking System

A microservices-based real-time order tracking platform built using Spring Boot, Kafka, Redis, and Docker.

---

## ✅ Features

- **Microservices Architecture**: Decoupled services for Orders, Tracking, and (upcoming) Notifications
- **Apache Kafka Integration**: Event-driven communication between services
- **Redis Caching**: Instant order lookup with key-based TTL support
- **Type-Safe Deserialization**: `ErrorHandlingDeserializer` setup for clean consumer recovery
- **Multiple Listener Containers**: Separate Kafka consumer factories per event type
- **Prometheus & Grafana**: Real-time metrics, JVM health, and Kafka observability
- **REST APIs**:
  - `POST /orders/createOrder`
  - `PUT /orders/updateOrderStatus/{id}?status=SHIPPED`
  - `GET /track/{id}`

---

## 🛠️ Tech Stack

| Component      | Tech                         |
|----------------|------------------------------|
| Language       | Java                         |
| Framework      | Spring Boot 3.5              |
| Messaging      | Apache Kafka                 |
| Cache          | Redis                        |
| Monitoring     | Prometheus + Grafana         |
| Build Tool     | Maven                        |
| Deployment     | Docker, Docker Compose       |

---

## 📂 Microservices

### 🧾 `orders-service`
Handles order creation and status updates. Publishes Kafka events.

### 📍 `tracking-service`
Listens to order and status events, caches them in Redis with TTL. Exposes tracking API.

### 📨 `notification-service`
Listens to Kafka events and logs mock email/SMS notifications.

---

## 🧪 Example API Calls

```bash
# Create Order
curl -X POST http://localhost:8081/orders/createOrder \
  -H "Content-Type: application/json" \
  -d '{
    "customerName": "Vikrant",
    "item": "Book",
    "quantity": 1
  }'

# Update Order Status
curl -X PUT http://localhost:8081/orders/updateOrderStatus/11?status=SHIPPED

# Track Order
curl -i http://localhost:8082/track/11

## 🚀 Running the Project

```bash
# Start Kafka, Redis, and Postgres via Docker
docker-compose up

# Start each service individually
cd orders-service
./mvnw spring-boot:run

cd tracking-service
./mvnw spring-boot:run


## 📊 Monitoring

Visit after running `docker compose up -d`:
- Prometheus: [http://localhost:9090](http://localhost:9090)
- Grafana: [http://localhost:3000](http://localhost:3000)
  - Login: `admin / admin`
  - Add Prometheus (http://prometheus:9090) as data source
  - Import dashboard ID `4701` for Spring Boot metrics


## 📈 Metrics

Prometheus exposes:
- `http_server_requests_seconds_count`
- `kafka_consumer_records_consumed_total`
- `redis_commands_processed_total`
- `jvm_memory_used_bytes`

Use Explore tab in Grafana to run queries.
