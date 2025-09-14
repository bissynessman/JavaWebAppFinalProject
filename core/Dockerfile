FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY target/core-1.1.1.jar core.jar
ENTRYPOINT ["java", "-jar", "core.jar"]