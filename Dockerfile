#Maven Build
FROM maven:3.9.4-amazoncorretto-17 AS builder
COPY pom.xml /app/
COPY src /app/src
RUN mvn -f /app/pom.xml clean package -DskipTests
#
##Run
FROM amazoncorretto:17
COPY --from=builder /app/target/employee-training-0.0.1-SNAPSHOT.jar employee-training.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "employee-training.jar"]