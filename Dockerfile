FROM openjdk:17-jdk
LABEL authors="gwanghun"
ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.properties /application.properties
ENTRYPOINT ["java", "-jar", "app.jar"]