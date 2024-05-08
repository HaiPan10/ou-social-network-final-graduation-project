FROM openjdk:17-jdk-alpine
WORKDIR /apigateway
EXPOSE 80
COPY ./target/*.jar apigateway.jar
CMD ["java", "-jar", "./apigateway.jar"]