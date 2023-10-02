#
# Build stage
#
FROM maven:3.8.2-jdk-17 AS build
COPY . .
RUN mvn clean install

#
# Run stage
#
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080