## Microservicio de Estadísticas de Clientes (ms-customer-stats)
Este proyecto es la solución a la prueba técnica para Desarrollador Java (Nivel Middle/Senior) de Muebles SAS. El objetivo es construir un microservicio reactivo encargado de recibir, validar y procesar estadísticas de interacción con usuarios.

### 📋 Tecnologías Utilizadas
- Java 17

- Spring Boot 3 con Spring WebFlux (Programación Reactiva)

- Project Reactor

- Gradle como gestor de dependencias

- Lombok para la reducción de código boilerplate

- MapStruct para el mapeo eficiente entre DTOs y modelos de dominio

- DynamoDB como base de datos NoSQL

- RabbitMQ como broker de mensajería para eventos

- Docker & Docker Compose para la gestión del entorno local

- JUnit 5 para pruebas unitarias y de integración

### 🏛️ Arquitectura
El proyecto está construido siguiendo los principios de Clean Architecture, separando claramente las responsabilidades en las siguientes capas:

Domain: Contiene los modelos y reglas de negocio puros.

Use Cases: Orquesta los flujos de la aplicación.

Entry Points: Expone la funcionalidad al exterior (API REST).

Driven Adapters: Implementa la comunicación con tecnologías externas (DynamoDB, RabbitMQ).

### 🚀 Puesta en Marcha
Sigue estos pasos para levantar y ejecutar el proyecto en tu entorno local.

Prerrequisitos
Java 17 o superior

Docker y Docker Compose

AWS CLI instalado y configurado (puedes usar credenciales falsas como se explica aquí)

1. Clonar el Repositorio
git clone <URL_DEL_REPOSITORIO>
cd ms-customer-stats

2. Levantar el Entorno con Docker Compose
Este comando iniciará los contenedores de DynamoDB y RabbitMQ.

docker-compose up -d

Puedes verificar que los servicios están corriendo con docker-compose ps.

DynamoDB estará disponible en el puerto 8000.

RabbitMQ Management estará disponible en http://localhost:15672 (user: guest, pass: guest).

3. Crear la Tabla en DynamoDB
Ejecuta el siguiente comando para crear la tabla customer_stats en tu instancia local de DynamoDB.

aws dynamodb create-table \
    --table-name customer_stats \
    --attribute-definitions AttributeName=timestamp,AttributeType=S \
    --key-schema AttributeName=timestamp,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://localhost:8000

4. Ejecutar la Aplicación
Finalmente, puedes ejecutar el microservicio usando el wrapper de Gradle.

./gradlew bootRun

La aplicación estará disponible en http://localhost:8080.

⚙️ Uso de la API
El servicio expone un único endpoint para procesar las estadísticas.

Endpoint: POST /api/customer-stats

### ✅ Ejemplo de Petición Exitosa
Para que la petición sea exitosa, el hash debe ser el MD5 correcto de los valores numéricos concatenados.

curl --location --request POST 'http://localhost:8080/api/customer-stats' \
--header 'Content-Type: application/json' \
--data-raw '{
    "totalContactoClientes": 250,
    "motivoReclamo": 25,
    "motivoGarantia": 10,
    "motivoDuda": 100,
    "motivoCompra": 100,
    "motivoFelicitaciones": 7,
    "motivoCambio": 8,
    "hash": "5484062a4be1ce5645eb414663e14f59"
}'

Respuesta esperada: 200 OK

❌ Ejemplo de Petición con Hash Inválido
curl --location --request POST 'http://localhost:8080/api/customer-stats' \
--header 'Content-Type: application/json' \
--data-raw '{
    "totalContactoClientes": 250,
    "motivoReclamo": 25,
    "motivoGarantia": 10,
    "motivoDuda": 100,
    "motivoCompra": 100,
    "motivoFelicitaciones": 7,
    "motivoCambio": 8,
    "hash": "hash_incorrecto"
}'

Respuesta esperada: 400 Bad Request

🧪 Ejecución de Pruebas
Para ejecutar el conjunto completo de pruebas (unitarias y de integración), utiliza el siguiente comando de Gradle:

./gradlew test

El reporte de cobertura de las pruebas se puede encontrar en build/reports/jacoco/test/html/index.html.
