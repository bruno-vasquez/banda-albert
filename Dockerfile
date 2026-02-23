# ====== STAGE 1: Build (Java 11) ======
FROM eclipse-temurin:11-jdk AS build

WORKDIR /app
COPY . .

WORKDIR /app/springboot
RUN chmod +x mvnw && ./mvnw -DskipTests clean package

# ====== STAGE 2: Run (Java 11 liviano) ======
FROM eclipse-temurin:11-jre-alpine

WORKDIR /app
COPY --from=build /app/springboot/target/*.jar app.jar

EXPOSE 9090
ENV PORT=9090
ENTRYPOINT ["sh","-c","java -Dserver.port=${PORT} -jar /app/app.jar"]