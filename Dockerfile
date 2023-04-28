# Use the official OpenJDK 8 image as the base image
FROM openjdk:8-jre-slim

# Set the working directory
WORKDIR /app

# Copy the JAR file from your build output to the working directory
COPY /build/libs/*.jar app.jar

# Expose the port on which your application will run
EXPOSE 8080

# Set the entrypoint for running the application
ENTRYPOINT ["java", "-jar", "app.jar"]
