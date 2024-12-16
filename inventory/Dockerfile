FROM maven:3.8.7-eclipse-temurin-19 AS build

# Copy dependency module
COPY ./message /message

# Build the module
WORKDIR /message
RUN mvn clean install

# Copy the Spring Boot app
COPY ./inventory /app

# Build the Spring Boot app
WORKDIR /app
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/inventory*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]