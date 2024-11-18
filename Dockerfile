FROM maven:3.8.6-openjdk-17-slim

WORKDIR /app

COPY . /app

RUN mvn clean package

EXPOSE 8080

CMD ["java", "-jar", "target/botTelRadar-0.0.1-SNAPSHOT.jar"]