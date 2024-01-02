# Use an official Maven image with Java
FROM maven:3.8.4-openjdk-17-slim

# Set the working directory within the container
WORKDIR /app

# Copy the POM file and source code
COPY pom.xml /app
COPY src /app/src

# Build the project
RUN mvn clean package

CMD ["java", "-jar", "target/user-service.jar"]

#
EXPOSE 8080