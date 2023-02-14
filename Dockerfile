FROM openjdk:17
MAINTAINER yusupovforwin@gmail.com
ARG JAR_FILE=target/*.jar
COPY ./target/Shop-app-0.0.1-SNAPSHOT.jar Shop-app-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "/Shop-app-0.0.1-SNAPSHOT.jar"]