# Etapa 1: construir la app con Maven
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copiar pom.xml y descargar dependencias (caché)
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copiar el código fuente
COPY src ./src

# Construir el jar sin ejecutar tests
RUN mvn clean package -DskipTests

# Etapa 2: ejecutar la app con Java
FROM eclipse-temurin:17-jdk

WORKDIR /app

# Copiar el jar desde la etapa anterior
COPY --from=build /app/target/*.jar app.jar

# Puerto donde correrá el microservicio
EXPOSE 8083

ENTRYPOINT ["java", "-jar", "app.jar"]
