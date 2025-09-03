# Use an official OpenJDK runtime as base image
FROM openjdk:17-jdk-slim

# Set the working directory
WORKDIR /app

# Copy Maven files first (for dependency caching)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn

# Download dependencies
RUN ./mvnw dependency:go-offline -B

# Copy the entire project
COPY . .

# Package the application (skip tests for faster build)
RUN ./mvnw clean package -DskipTests

# Expose port 8080 (Spring Boot default)
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "target/notesapp-0.0.1-SNAPSHOT.jar"]

