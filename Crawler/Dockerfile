# Use an official Maven image with Java
FROM maven:3.8.4-openjdk-17-slim

# Set the working directory within the container
WORKDIR /app

COPY . /app

# Build the project
RUN mvn clean package

# Run the Java application
CMD ["java", "-jar", "target/crawler.jar"]

#
EXPOSE 8080