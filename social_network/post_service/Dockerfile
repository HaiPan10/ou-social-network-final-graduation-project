FROM openjdk:17-jdk-alpine
WORKDIR /postservice
EXPOSE 8085
COPY ./target/*.jar postservice.jar
CMD ["java", "-jar", "./postservice.jar"]