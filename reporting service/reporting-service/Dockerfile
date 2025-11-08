# Imagen base con Java 17
FROM eclipse-temurin:17-jdk

# Carpeta de trabajo dentro del contenedor
WORKDIR /app

# Copiamos el archivo JAR generado en tu PC (luego de compilar)
COPY target/reporting-service-0.0.1-SNAPSHOT.jar reporting-service.jar

# Exponemos el puerto que usa tu aplicación
EXPOSE 8083

# Comando para ejecutar tu microservicio
ENTRYPOINT ["java", "-jar", "reporting-service.jar"]
