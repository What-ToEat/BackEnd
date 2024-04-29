FROM openjdk:17-jdk
LABEL authors="gwanghun"

CMD ["./gradlew", "clean", "build"]

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
COPY src/main/resources/application.properties /application.properties

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]