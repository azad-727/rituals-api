# Stage 1: Build the application using Maven and Java 25
FROM maven:3.9-eclipse-temurin-25 AS build
WORKDIR /app
# Copy the pom.xml and source code
COPY pom.xml .
COPY src ./src
# Build the JAR file, skipping tests to speed up deployment
RUN mvn clean package -DskipTests

# Stage 2: Run the application using a lightweight Java 25 environment
FROM eclipse-temurin:25-jre
WORKDIR /app
# Copy the built JAR from Stage 1
COPY --from=build /app/target/*.jar app.jar
# Expose the standard Spring Boot port
EXPOSE 8080
# Start the server
ENTRYPOINT ["java", "-jar", "app.jar"]