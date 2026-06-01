# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

# Copy Gradle wrapper and config first — better Docker layer caching.
# If only source changes, dependencies don't need to be re-downloaded.
COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

# Make the wrapper executable and pre-fetch dependencies.
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

# Now copy source code and build.
COPY src src
RUN ./gradlew bootJar --no-daemon

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:21-jre-alpine

# Create a non-root user — don't run Java as root.
RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

# Copy only the built jar from the builder stage.
COPY --from=builder /build/build/libs/*.jar app.jar

# Drop root.
RUN chown app:app /app/app.jar
USER app

EXPOSE 8081

# Use exec form so Java is PID 1 — signal handling works correctly.
ENTRYPOINT ["java", "-jar", "/app/app.jar"]