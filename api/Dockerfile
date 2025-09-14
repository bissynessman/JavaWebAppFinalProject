FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY target/api-1.1.1.jar api.jar
ENTRYPOINT ["java", "-jar", "api.jar"]
