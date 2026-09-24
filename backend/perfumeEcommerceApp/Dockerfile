# Stage 1: Build the application
FROM eclipse-temurin:25-jdk AS builder

WORKDIR /app

COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src src

RUN chmod +x mvnw
RUN ./mvnw clean package -DskipTests


# Stage 2: Run the application
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=builder /app/target/perfume-app.jar perfume-app.jar

ENTRYPOINT ["java", "-jar", "perfume-app.jar"]