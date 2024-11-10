# 빌드 스테이지
FROM gradle:8.5-jdk17 AS builder

# 작업 디렉토리 설정
WORKDIR /build

# Gradle 설정 파일들 복사
COPY gradle gradle
COPY gradlew .
COPY gradlew.bat .
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사
COPY src src

# Gradle 빌드 캐시 설정
RUN mkdir -p /root/.gradle && \
    echo "org.gradle.daemon=false" >> /root/.gradle/gradle.properties

# 애플리케이션 빌드
RUN chmod +x ./gradlew && \
    ./gradlew bootJar --no-daemon

# 실행 스테이지
FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=builder /build/build/libs/*.jar app.jar

# JVM 설정
ENV JAVA_OPTS="-XX:+UseContainerSupport \
               -XX:MaxRAMPercentage=75.0 \
               -Dfile.encoding=UTF-8 \
               -Djava.security.egd=file:/dev/./urandom \
               -Duser.timezone=Asia/Seoul"

EXPOSE 8080
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]