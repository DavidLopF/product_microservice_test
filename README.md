# Product Service

Este es un microservicio desarrollado en Spring Boot para la gestión de productos. El servicio proporciona una API RESTful para realizar operaciones CRUD sobre productos, incluyendo la gestión de imágenes a través de AWS S3.

## Características

- API RESTful para gestión de productos
- Integración con PostgreSQL para persistencia de datos
- Almacenamiento de imágenes en AWS S3
- Documentación de API con Swagger/OpenAPI
- Seguridad implementada con Spring Security
- Manejo de reintentos para operaciones críticas
- Validación de datos

## Requisitos Previos

- Java 17 o superior
- Maven 3.6 o superior
- PostgreSQL
- AWS S3 (para almacenamiento de imágenes)
- Docker (opcional, para ejecución en contenedor)

## Configuración del Entorno

1. Clonar el repositorio:
```bash
git clone [URL_DEL_REPOSITORIO]
cd product-service
```

2. Configurar las variables de entorno:
   - Copiar el archivo `.env.example` a `.env`
   - Configurar las siguientes variables:
     - `DB_URL`: URL de conexión a PostgreSQL
     - `DB_USERNAME`: Usuario de la base de datos
     - `DB_PASSWORD`: Contraseña de la base de datos
     - `AWS_ACCESS_KEY`: Clave de acceso de AWS
     - `AWS_SECRET_KEY`: Clave secreta de AWS
     - `AWS_REGION`: Región de AWS
     - `AWS_BUCKET_NAME`: Nombre del bucket S3

## Ejecución Local

1. Compilar el proyecto:
```bash
mvn clean install
```

2. Ejecutar la aplicación:
```bash
mvn spring-boot:run
```

La aplicación estará disponible en `http://localhost:8080`

## Documentación de la API

Una vez que la aplicación esté en ejecución, puedes acceder a la documentación de la API en:
- Swagger UI: `http://localhost:8080/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8080/v3/api-docs`

## Ejecución con Docker

1. Construir la imagen:
```bash
docker build -t product-service .
```

2. Ejecutar el contenedor:
```bash
docker run -p 8080:8080 --env-file .env product-service
```

## Estructura del Proyecto

```
src/
├── main/
│   ├── java/
│   │   └── co/
│   │       └── corp/
│   │           └── linktic/
│   │               ├── config/        # Configuraciones
│   │               ├── controller/    # Controladores REST
│   │               ├── dto/           # Objetos de Transferencia de Datos
│   │               ├── model/         # Entidades
│   │               ├── repository/    # Repositorios JPA
│   │               ├── service/       # Lógica de negocio
│   │               └── util/          # Utilidades
│   └── resources/
│       ├── application.properties    # Configuración de la aplicación
│       └── application.yml           # Configuración alternativa
```

## Contribución

1. Fork el proyecto
2. Crear una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abrir un Pull Request

