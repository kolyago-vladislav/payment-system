FROM gradle:8.14.2-jdk24 AS build
ARG NEXUS_URL=http://nexus:8081/repository/maven-public/
ENV NEXUS_URL=${NEXUS_URL}
WORKDIR /home/gradle/project

COPY build.gradle.kts .
COPY settings.gradle.kts .
COPY gradle ./gradle
COPY src ./src

RUN gradle --no-daemon clean build

FROM eclipse-temurin:24-jre
WORKDIR /app
COPY --from=build /home/gradle/project/build/libs/*.jar app.jar

EXPOSE 8080
CMD ["java", "-jar", "app.jar"]