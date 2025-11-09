FROM eclipse-temurin:21-jdk-alpine AS build

# build dependencies
COPY ./message /message
WORKDIR /message
RUN ./gradlew clean build publishToMavenLocal

# build order application
COPY ./order /app
WORKDIR /app
RUN ./gradlew clean bootJar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/order*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]