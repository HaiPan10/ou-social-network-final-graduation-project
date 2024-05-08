FROM openjdk:17-jdk-alpine
WORKDIR /mailservice
EXPOSE 8084
COPY ./target/*.jar mailservice.jar
CMD ["java", "-jar", "./mailservice.jar"]