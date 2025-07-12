# 📦 Order Tracking System

A microservices-based real-time order tracking platform built using Spring Boot, Kafka, Redis, and Docker.

---

## ✅ Features

- **Microservices Architecture**: Decoupled services for Orders, Tracking, and (upcoming) Notifications
- **Apache Kafka Integration**: Event-driven communication between services
- **Redis Caching**: Instant order lookup with key-based TTL support
- **Type-Safe Deserialization**: `ErrorHandlingDeserializer` setup for clean consumer recovery
- **Multiple Listener Containers**: Separate Kafka consumer factories per event type
- **REST APIs**:
  - `POST /orders/createOrder`
  - `PUT /orders/updateOrderStatus/{id}?status=SHIPPED`
  - `GET /track/{id}`

---

## 🛠️ Tech Stack

| Component      | Tech                         |
|----------------|------------------------------|
| Language       | Java 17                      |
| Framework      | Spring Boot 3.5              |
| Messaging      | Apache Kafka                 |
| Cache          | Redis                        |
| Build Tool     | Maven                        |
| Deployment     | Docker, Docker Compose       |

---

## 📂 Microservices

- `orders-service` → Order creation & updates (produces Kafka events)
- `tracking-service` → Listens for events and caches order status in Redis
- `notification-service` *(coming soon)* → Notifies users of updates

---

## 🚀 Running the Project

```bash
# Start Kafka, Redis, and Postgres via Docker
docker-compose up

# Start each service individually
cd orders-service
./mvnw spring-boot:run

cd tracking-service
./mvnw spring-boot:run
