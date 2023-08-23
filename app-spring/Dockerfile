FROM bellsoft/liberica-openjdk-alpine:17
COPY build/libs/tracker-app-spring-0.0.3.jar /app/app.jar

ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-jar", "/app/app.jar"]
