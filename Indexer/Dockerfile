# Use an official Maven image with Java
FROM maven:3.8.4-openjdk-17-slim

# Set the working directory within the container
WORKDIR /app

# Copy the POM file and source code
COPY pom.xml /app
COPY src /app/src

# Define a volume to mount the host directory
VOLUME /home/adambreznen/OneDrive/DocumentsRepository

# Build the project
RUN mvn clean package

# Run the Java application
CMD ["java", "-jar", "target/indexer.jar", "./DocumentsRepository"]

#
EXPOSE 8080