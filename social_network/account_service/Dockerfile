FROM openjdk:17-jdk-alpine
WORKDIR /accountservice
EXPOSE 8082
COPY ./target/*.jar accountservice.jar
CMD ["java", "-jar", "./accountservice.jar"]