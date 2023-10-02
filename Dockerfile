#
# Build stage
#
FROM maven:3.9.4-eclipse-temurin-17-alpine AS build
COPY . .
RUN mvn clean install

#
# Run stage
#
FROM eclipse-temurin:17-jdk-alpine
VOLUME /tmp
COPY --from=build /target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080