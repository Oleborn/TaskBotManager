FROM openjdk:21-jdk

WORKDIR /app

COPY target/TaskBot-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8443

ENTRYPOINT ["java", "-jar", "/app/app.jar"]