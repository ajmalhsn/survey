# Multi-stage Dockerfile
# Build stage: use Maven with JDK 17 to build the fat jar
FROM maven:3.9.4-eclipse-temurin-17 AS build
WORKDIR /workspace

# copy only what is necessary for dependency resolution first (speeds up Docker cache)
COPY pom.xml .
COPY mvnw .
COPY .mvn .mvn
COPY src ./src

# Build the project (skip tests by default in image build)
RUN mvn -B -DskipTests package

# Run stage: lightweight JRE to run the Spring Boot application
FROM eclipse-temurin:17-jre
WORKDIR /app
ARG JAR_FILE=target/*.jar

# Copy the built jar from the build stage
COPY --from=build /workspace/${JAR_FILE} app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
