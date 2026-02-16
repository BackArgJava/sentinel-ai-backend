# 1. Use a lightweight, official Java 21 environment
FROM eclipse-temurin:21-jdk-alpine

# 2. Set the working directory inside our virtual machine
WORKDIR /app

# 3. Copy your compiled Java application into the container
COPY target/*.jar app.jar

# 4. Expose the port your Spring Boot API uses
EXPOSE 8081

# 5. The exact command to run the server when the container starts
ENTRYPOINT ["java", "-jar", "app.jar"]