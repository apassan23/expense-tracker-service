#
# Build stage
#
FROM maven:3.9.4-amazoncorretto-20-al2023 AS build
COPY . .
RUN mvn clean install

#
# Run stage
#
FROM amazoncorretto:20-alpine-jdk
VOLUME /tmp
RUN ls
COPY --from=build expense-tracker-app/target/*.jar app.jar
ENTRYPOINT ["java","-jar","/app.jar"]
EXPOSE 8080