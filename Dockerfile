FROM openjdk:17-alpine

ARG JAR_FILE=build/libs/*SNAPSHOT.jar

COPY ${JAR_FILE} fourj.jar

ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=develop", "/fourj.jar"]
