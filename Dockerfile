FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/*.jar

COPY ${JAR_FILE} fourj.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "/fourj.jar"]