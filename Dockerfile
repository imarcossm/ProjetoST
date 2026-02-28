FROM eclipse-temurin:17-jdk-alpine AS builder

WORKDIR /app
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
# Resolve dependencies
RUN ./mvnw dependency:go-offline

COPY src ./src
# Build the application
RUN ./mvnw clean package -DskipTests

# Run stage
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
