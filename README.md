# Microservices Demo

A demo application using the microservices architecture.

## Prerequisites

- Java 17+
- A Docker environment supported by Testcontainers: <https://www.testcontainers.org/supported_docker_environment/> (a recent local Docker installation is enough)

## Getting Started

Clone the repository and run the following command to build the project:

```shell
./mvnw clean verify
```

## Running the service in development

Move to the `people-service` directory:

```shell
cd people-service
```

Run the following command:

```shell
./mvnw spring-boot:run
```

Try the services at <http://localhost:8080/swagger-ui.html>.

Try the web application using the service at <http://localhost:8082/>.

### Running the application with Docker Compose

A `docker-compose-.yml` file is provided to run the service with Docker Compose. In the main directory:

```shell
docker compose -f docker-compose.yml up
```

Try the services at <http://localhost:8081/swagger-ui.html>.

Try the web application using the service at <http://localhost:8082/>.

