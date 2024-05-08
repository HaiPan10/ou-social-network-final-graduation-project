FROM openjdk:17-jdk-alpine
WORKDIR /adminservice
EXPOSE 8081
COPY ./target/*.jar adminservice.jar
CMD ["java", "-jar", "./adminservice.jar"]