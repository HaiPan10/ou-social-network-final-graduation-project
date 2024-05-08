FROM openjdk:17-jdk-alpine
WORKDIR /eurekaserver
EXPOSE 8761
COPY ./target/*.jar eurekaserver.jar
CMD ["java", "-jar", "./eurekaserver.jar"]