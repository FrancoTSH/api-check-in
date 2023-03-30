# Simulación check-in aerolínea

El proyecto se trata de una pequeña API, la cual permite hacer check-in automático a los pasajeros de una aerolínea. Las tecnologias que se utilizaron son las siguientes:

- Java 17
- Spring Boot
- Spring Data JPA
- MySQL
- Maven
- Swagger

Prerequisitos: [Java 17](https://www.oracle.com/java/technologies/downloads/) , [Maven](https://maven.apache.org/download.cgi), un IDE o editor de código.

# Para empezar

Para clonar y ejecutar esta aplicación por ti mismo, asegúrate de que tienes al menos Java 17 y todas las cosas del JDK (incluyendo JRE), Maven para construir las dependencias y algún IDE o editor de código. Después de eso, sigue las siguientes instrucciones:

- Para instalar este proyecto, ejecuta el siguiente comando:

```bash
git clone https://github.com/FrancoTSH/bsale-check-in.git
```

Esto generará una copia del proyecto instalada localmente. Para configurar todas sus dependencias, sigue las instrucciones debajo:

- Abre el proyecto con tu editor de código o IDE preferido.

- Ejecuta el siguiente comando en la carpeta del proyecto:
  `mvn spring-boot:run`, o con ayuda de tu IDE o editor de codigo ejecuta el proyecto (esto deberia instalar las dependencias necesarias tambien).

- Los endpoints estarán localizados en 'http://localhost:8080/api/v1'. Se puede configurar el puerto y otras propiedades en `src/main/resources/application.properties`

**Ahora, eres capaz de ejecutar este proyecto localmente.**

## Si solo deseas usar la API sin clonar el proyecto

- Abre un cliente REST como Postman para hacer las peticiones.
- Accede a los endpoints a traves de la siguiente URL:

      http://bsale-check-in-production.up.railway.app/api/v1

## Documentación de la API

La API solo tiene un endpoint, el cual sirve para hacer el check-in automático de los pasajeros según el vuelo indicado.

- Método: `GET`
- Ruta: `http://bsale-check-in-production.up.railway.app/api/v1/flights/{id}/passengers`

- Estructura de la respuesta:

      {
        "code": 0,
        "data": {
          "flightId": 0,
          "takeOffDateTime": 0,
          "takeOffAirport": "string",
          "landingDateTime": 0,
          "landingAirport": "string",
          "airplaneId": 0,
          "passengers": [
            {
              "passengerId": 0,
              "dni": 0,
              "name": "string",
              "age": 0,
              "country": "string",
              "boardingPassId": 0,
              "purchaseId": 0,
              "seatTypeId": 0,
              "seatId": 0
            }
          ]
        }
      }

Si quieres conocer más detalles ingresa a la siguiente URL:

    https://bsale-check-in-production.up.railway.app/api/v1/docs/swagger-ui/index.html
