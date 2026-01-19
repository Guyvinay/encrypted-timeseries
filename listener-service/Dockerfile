FROM eclipse-temurin:21-jdk-alpine

WORKDIR /app

COPY target/listener-service-*.jar /app/listener-service.jar

EXPOSE 9000
EXPOSE 8080

CMD ["java", "-jar", "/app/listener-service.jar"]
