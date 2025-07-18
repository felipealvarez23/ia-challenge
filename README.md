## Microservicio de Estadísticas de Clientes (ms-customer-stats)
### Introducción
Este monorepositorio  contiene la solución al reto técnico para Desarrollador Java (Middle/Senior) de Bancolombia. El reto plantea un escenario ficticio donde la empresa Muebles SAS busca modernizar su arquitectura a través de un ecosistema de microservicios para potenciar la experiencia de sus clientes.

El reto propone la implementación de un microservicio el cual actúa como el punto de entrada principal para las estadísticas de interacción con usuarios. Su responsabilidad es recibir, validar y procesar estos datos de forma reactiva y asíncrona. La integridad de los datos se asegura mediante la validación de un hash MD5.

La solución emplea un stack tecnológico moderno, incluyendo Spring WebFlux para la gestión de peticiones no bloqueantes, DynamoDB para la persistencia NoSQL y RabbitMQ para la publicación de eventos. El diseño se adhiere estrictamente a los principios de Clean Architecture, lo que resulta en un código desacoplado, mantenible y altamente escalable.

###  Prerequisitos
- Java 17 o superior
- Docker y Docker Compose
- AWS CLI instalado y configurado

###  Ejecución ambiente local
Se deben ejecutar los siguiente pasos para levantar el proyecto en el  ambiente local.

1. Clonar el Repositorio y navegar al folder del microservicio
```
git clone https://github.com/felipealvarez23/ia-challenge.git -b trunk
cd ms-customer-stats
```

2. Levantar el Entorno con Docker Compose
Este comando iniciará los contenedores de DynamoDB y RabbitMQ.

```
docker-compose up -d
```

Puedes verificar que los servicios están corriendo con docker-compose ps.

DynamoDB estará disponible en el puerto 8000.

RabbitMQ Management estará disponible en http://localhost:15672 (user: guest, pass: guest).

3. Crear la Tabla en DynamoDB
Ejecuta el siguiente comando para crear la tabla customer_stats en tu instancia local de DynamoDB.

```
aws dynamodb create-table \
    --table-name customer_stats \
    --attribute-definitions AttributeName=timestamp,AttributeType=S \
    --key-schema AttributeName=timestamp,KeyType=HASH \
    --provisioned-throughput ReadCapacityUnits=5,WriteCapacityUnits=5 \
    --endpoint-url http://localhost:8000
```

4. Ejecutar la Aplicación
Finalmente, puedes ejecutar el microservicio usando el wrapper de Gradle.

```
./gradlew bootRun
```

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

./gradlew clean build jacocoMergedReport

El reporte de cobertura de las pruebas se puede encontrar en build/reports/jacoco/test/html/index.html.
