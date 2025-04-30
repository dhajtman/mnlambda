# Dockerfile
FROM ghcr.io/graalvm/graalvm-ce:latest as builder

# Install Native Image
RUN gu install native-image

# Set working directory
WORKDIR /app

# Copy project files
COPY . .

# Run Maven build
CMD ["./mvnw", "package", "-Dpackaging=docker-native", "-Dmicronaut.runtime=lambda", "-Pgraalvm"]