# Usar imagen oficial de Maven con Java 17
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app
COPY . .
RUN mvn -DskipTests clean package

# Imagen ligera solo con Java para producción
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","/app/app.jar"]