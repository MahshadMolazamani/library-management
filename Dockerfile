FROM gradle:8-jdk-21-and-22 AS build

WORKDIR /home/app/code
COPY gradle ./gradle
COPY src ./src
COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradlew .
RUN gradle build -x test --no-daemon

FROM eclipse-temurin:21-jdk
WORKDIR /home/app/code
COPY --from=build /home/app/code/build/libs/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
