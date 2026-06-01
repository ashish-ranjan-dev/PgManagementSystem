# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /build

COPY gradlew .
COPY gradle gradle
COPY build.gradle settings.gradle ./

RUN chmod +x gradlew && ./gradlew dependencies --no-daemon || true

COPY src src
RUN ./gradlew bootJar --no-daemon

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:21-jre-alpine

RUN addgroup -S app && adduser -S app -G app

WORKDIR /app

COPY --from=builder /build/build/libs/*.jar app.jar

RUN chown app:app /app/app.jar
USER app

EXPOSE 8082

ENTRYPOINT ["java", "-jar", "/app/app.jar"]