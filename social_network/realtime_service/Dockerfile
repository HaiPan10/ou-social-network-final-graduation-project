FROM openjdk:17-jdk-alpine
WORKDIR /realtimeservice
EXPOSE 8086
COPY ./target/*.jar realtimeservice.jar
CMD ["java", "-jar", "./realtimeservice.jar"]