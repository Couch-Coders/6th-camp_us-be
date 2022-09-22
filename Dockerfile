FROM openjdk:11

WORKDIR /app

COPY build/libs/camping-0.0.1-SNAPSHOT.jar /app/camping.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/camping.jar", "--spring.profiles.active=local", "--spring.datasource.url= jdbc:mysql://host.docker.internal:3306/camp"]