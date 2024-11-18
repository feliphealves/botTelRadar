FROM openjdk:17-slim

WORKDIR /app

# Instalar Maven manualmente
RUN apt-get update && apt-get install -y maven

COPY . /app

RUN chmod +x ./mvnw
RUN ./mvnw clean package

EXPOSE 8080

CMD ["java", "-jar", "target/botTelRadar-0.0.1-SNAPSHOT.jar"]