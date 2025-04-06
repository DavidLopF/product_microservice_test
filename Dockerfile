# Etapa 1: Construcción de la aplicación con Maven
FROM maven:3.8.5-openjdk-17 AS builder
WORKDIR /app
# Copia el archivo pom.xml y las dependencias para aprovechar el cache
COPY pom.xml .
RUN mvn dependency:go-offline -B
# Copia el resto del código fuente
COPY src ./src
# Empaqueta la aplicación (omite los tests si lo prefieres)
RUN mvn clean package -DskipTests

# Etapa 2: Imagen de ejecución con OpenJDK
FROM openjdk:17-jdk-alpine
WORKDIR /app
# Copia el JAR construido desde la etapa builder
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]