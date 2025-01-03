FROM jelastic/maven:3.9.5-openjdk-21 AS build

# Copy dependency module
COPY ./message /message

# Build the module
WORKDIR /message
RUN mvn clean install

# Copy the Spring Boot app
COPY ./order /app

# Build the Spring Boot app
WORKDIR /app
RUN mvn clean package -DskipTests

FROM openjdk:21-jdk-slim
WORKDIR /app
COPY --from=build /app/target/order*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]