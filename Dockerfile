FROM maven:3.9.4-eclipse-temurin-17-alpine

WORKDIR /app

# Copy Maven configuration files
COPY pom.xml .
COPY testng.xml .
COPY regression-testng.xml .

# Download dependencies first (better caching)
RUN mvn dependency:go-offline -B

# Copy all source code (includes your testdata classes)
COPY src ./src

# Create output directories
RUN mkdir -p reports logs

# Environment variables
ENV MAVEN_OPTS="-Xmx1024m"

# Run tests
CMD ["mvn", "clean", "test"]