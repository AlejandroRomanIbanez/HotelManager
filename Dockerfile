FROM openjdk:11-jre-slim

WORKDIR /app

COPY target/Hotel-0.0.1-SNAPSHOT.jar /app/Hotel-0.0.1-SNAPSHOT.jar

EXPOSE 5454

CMD ["java", "-jar", "Hotel-0.0.1-SNAPSHOT.jar"]
