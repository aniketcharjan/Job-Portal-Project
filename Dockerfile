# ================================
# STAGE 1: Build the application
# ================================
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /app

# Copy pom.xml first (better caching)
COPY pom.xml .
RUN mvn dependency:go-offline

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests


# ================================
# STAGE 2: Run the application
# ================================
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Copy only the JAR from builder stage
COPY --from=builder /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
