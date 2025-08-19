FROM maven:3.9.6-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -DskipTests

FROM tomcat:10.1.25-jdk21

WORKDIR /usr/local/tomcat/webapps

COPY --from=build /app/target/weather.war ./ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]