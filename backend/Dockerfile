# Use the official OpenJDK 11 base image
FROM gradle:jdk17-alpine as builder

# Set the working directory
WORKDIR /app

COPY . .

RUN ./gradlew bootJar

FROM openjdk:17

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# Expose the application's port
EXPOSE 8080

# Run the application
CMD ["java", "-jar", "app.jar"]

