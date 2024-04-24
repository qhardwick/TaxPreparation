FROM openjdk:17-alpine

WORKDIR /app

COPY target/TaxStorm-0.0.1-SNAPSHOT.jar /app/TaxStorm.jar

EXPOSE 8080

CMD ["java", "-jar", "TaxStorm.jar"]