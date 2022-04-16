FROM openjdk:17-alpine as BUILD_STAGE

COPY ./.mvn ./.mvn
COPY ./mvnw ./mvnw
COPY ./pom.xml ./pom.xml

RUN ./mvnw dependency:go-offline -B

COPY ./src ./src

RUN ./mvnw package spring-boot:repackage -DskipTests

###

FROM openjdk:17-alpine

COPY --from=BUILD_STAGE target/projectx*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]