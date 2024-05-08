FROM openjdk:17-jdk-alpine
WORKDIR /commentservice
EXPOSE 8083
COPY ./target/*.jar commentservice.jar
CMD ["java", "-jar", "./commentservice.jar"]