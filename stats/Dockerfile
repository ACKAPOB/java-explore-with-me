FROM amazoncorretto:11-alpine-jdk
COPY target/*.jar main_service_app.jar
ENTRYPOINT ["java", "-jar", "/main_service_app.jar"]