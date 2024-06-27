# Use an appropriate base image with JDK and Maven installed
FROM maven:3.8.4-openjdk-17-slim AS build

# Set working directory
WORKDIR /app

# Copy only the pom.xml to resolve dependencies
COPY pom.xml .

# Download dependencies and plugins into a separate layer
RUN mvn dependency:go-offline

# Copy the entire source code
COPY src ./src

# Compile and build the Spring Boot application
RUN mvn clean package -DskipTests

# Stage 2: Use a smaller base image to create the final image
FROM openjdk:17-jdk-slim

# Set working directory
WORKDIR /app

# Copy the built JAR from the previous stage
COPY --from=build /app/target/Hotel-0.0.1-SNAPSHOT.jar /app/hotel.jar

# Add additional files if needed (e.g., configuration files)
COPY docker/secrets/firebase_credentials.json /app/src/main/resources/firebase_credentials.json
COPY src/main/resources/secret.properties /app/secret.properties

RUN apt-get update && apt-get install -y netcat


# Expose the port
EXPOSE 5454

# Define the command to run your application
CMD ["java", "-jar", "hotel.jar"]
