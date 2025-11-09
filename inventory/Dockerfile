FROM eclipse-temurin:21-jdk-alpine AS build

# build dependencies
COPY ./message /message
WORKDIR /message
RUN ./gradlew clean build publishToMavenLocal

# build inventory application
COPY ./inventory /app
WORKDIR /app
RUN ./gradlew clean bootJar

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/build/libs/inventory*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]