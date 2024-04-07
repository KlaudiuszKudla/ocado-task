FROM eclipse-temurin:21-jdk-alpine as builder
WORKDIR /opt/app
COPY . .

RUN apk --no-cache add maven
RUN mvn clean package
FROM eclipse-temurin:21-jre-alpine
WORKDIR /opt/app
COPY --from=builder /opt/app/target/ocado-task-jar-with-dependencies.jar /opt/app/ocado-task-jar-with-dependencies.jar
COPY src/main/resources/config.json /opt/app/src/main/resources/config.json

ENTRYPOINT ["java", "-jar", "/opt/app/ocado-task-jar-with-dependencies.jar"]
