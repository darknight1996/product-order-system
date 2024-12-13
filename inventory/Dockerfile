# Step 1: Use a Maven image to build the application
FROM maven:3.8.7-eclipse-temurin-19 AS build
WORKDIR /app
# Copy project files to the container
COPY pom.xml .
COPY src ./src
# Build the application
RUN mvn clean package -DskipTests

# Step 2: Use a lightweight Java 21 runtime image for the final application
FROM openjdk:21-jdk-slim
WORKDIR /app
# Copy the built application jar from the previous step
COPY --from=build /app/target/inventory*.jar app.jar
# Expose the application port
EXPOSE 8080
# Run the application
ENTRYPOINT ["java", "-jar", "app.jar"]