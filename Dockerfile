FROM maven:3.9.9-eclipse-temurin-17 AS build
LABEL authors="vromanyu"

COPY pom.xml ./
COPY .mvn ./.mvn
COPY src ./src
RUN mvn clean install -DskipTests

FROM openjdk:17-oracle
WORKDIR /jwt_security
COPY --from=build ./target/*.jar ./jwt_security.jar
ENTRYPOINT ["java", "-jar", "jwt_security.jar"]
