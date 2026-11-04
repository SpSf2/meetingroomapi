# 1. Usar una imagen oficial de OpenJDK con Java 25 para compilar y ejecutar
FROM eclipse-temurin:25-jdk

# 2. Definir el directorio de trabajo dentro del contenedor
WORKDIR /app

# 3. Copiar el archivo Maven Wrapper y el pom.xml para descargar dependencias primero (optimiza la caché)
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# 4. Descargar las dependencias (esto acelera las construcciones futuras)
RUN ./mvnw dependency:go-offline

# 5. Copiar el resto del código fuente del proyecto
COPY src ./src

# 6. Empaquetar la aplicación ignorando las pruebas unitarias por ahora para agilizar
RUN ./mvnw clean package -DskipTests

# 7. Exponer el puerto 8080 donde corre Spring Boot
EXPOSE 8080

# 8. Comando para arrancar la aplicación cuando inicie el contenedor
ENTRYPOINT ["java", "-jar", "target/meetingroomapi-0.0.1-SNAPSHOT.jar"]