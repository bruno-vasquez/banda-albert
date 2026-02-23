# ====== STAGE 1: Build (Maven + Java 11) ======
FROM maven:3.9.6-eclipse-temurin-11 AS build

WORKDIR /app
COPY . .
RUN mvn -DskipTests clean package

# ====== STAGE 2: Run (Java 11 liviano) ======
FROM eclipse-temurin:11-jre-alpine

WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Render define PORT en runtime
EXPOSE 8080
ENV PORT=8080

ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT} -jar /app/app.jar"]