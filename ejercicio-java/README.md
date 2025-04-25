# Spring Boot BCI Project

Este proyecto es un microservicio desarrollado en Spring Boot utilizando Java 8, con Gradle 7.4 y SpringBoot 2.5.14 , que permite la creación y consulta de usuarios mediante los endpoints `/sign-up` y `/login`.

## Características

- **Endpoints:**
  - `POST /sign-up`: Crea un usuario, valida email y contraseña, encripta la contraseña con BCrypt, genera token JWT y persiste en H2.
  - `POST /login`: Consulta el usuario utilizando el token, renovándolo y actualizando el último login.

- **Validaciones:**
  - **Email:** Debe tener formato correcto (ej. aaaaaaa@undominio.algo).
  - **Contraseña:** Debe tener solo una mayúscula y solamente dos números, con letras minúsculas, longitud entre 8 y 12 caracteres.

- **Manejo de Errores:**  
  Los errores se retornan en formato JSON utilizando una clase `ErrorResponse`.

- **Pruebas Unitarias:**  
  Se incluye cobertura de pruebas superior al 80%.

## Instrucciones

1. Clonar el repositorio.
2. Ejecutar `./gradlew build` para construir el proyecto.
3. Ejecutar el proyecto con `./gradlew bootRun`.

Swagger: http://localhost:8080/swagger-ui.html

Sign-up curl:
curl -X POST http://localhost:8080/sign-up \
-H "Content-Type: application/json" \
-d '{
  "name": "John Doe",
  "email": "johndoe@example.com",
  "password": "Password1",
  "phones": [
    {
      "number": "123456789",
      "citycode": "1",
      "countrycode": "57"
    }
  ]
}'

Login curl:

curl --location --request POST 'http://localhost:8080/login' \
--header 'Authorization: Bearer ' \
--data ''
