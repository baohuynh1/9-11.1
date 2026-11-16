# ===== Stage 1: build JAR =====
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /workspace

# Copy file cấu hình Gradle trước để tận dụng cache layer khi chỉ sửa code
COPY gradlew settings.gradle build.gradle gradle.properties ./
COPY gradle ./gradle
RUN chmod +x gradlew && ./gradlew dependencies --no-daemon > /dev/null

COPY src ./src
RUN ./gradlew bootJar --no-daemon

# ===== Stage 2: runtime =====
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY --from=build /workspace/build/libs/account-service.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
