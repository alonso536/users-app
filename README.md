# Users App - Alonso Díaz

## Tecnologías

- Java
- Spring Boot
- Spring Security - JWT
- MySQL
- JPA - Hibernate
- JUnit 5 - Mockito
- Swagger
- Google Sign In - Por implementar
- Cloudinary - Por implementar

## Modelo relacional

<image src="https://res.cloudinary.com/dufgu6awb/image/upload/v1703031961/g9gxfclqztswaw9yevmh.jpg" alt="Modelo relacional" width="640px">

## Rutas

- Documentación: `/swagger-ui.html`
- POST - Iniciar sesión: `/login`
- POST - Crear usuario: `/api/users`
- GET - Listar usuarios: `/api/users`
- GET - Listar usuarios con paginación: `/api/users/page/{page}`
- GET - Ver usuario: `/api/users/{term}` se puede buscar por id o por slug
- PUT - Actualizar usuario `/api/users/{id}`
- PATCH - Actualizar contraseña del usuario `/api/users/{id}/password`
- DELETE - Borrar usuario `/api/users/{id}` cambia el campo active del usuario a false

## Pasos para levantar proyecto

1. Renombrar archivo `application.example.properties` a `application.properties`
2. Rellenar las propiedades correspondientes
3. Levantar base de datos `docker compose up`, (Debe tener instalado docker)
4. Conectarse a la base de datos y ejecutar el archivo `import.sql`

## Credenciales del admin

- Username: admin
- Password: ASdf123$
