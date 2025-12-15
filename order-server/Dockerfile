FROM gradle:8.7-jdk17 AS builder
WORKDIR /app

# 루트 전체 복사
COPY . .

# 루트 gradlew 권한 설정
# 멀티모듈 Gradle 프로젝트는 무조건 “프로젝트 전체(=루트 + 모든 모듈)”를 Docker build context 안으로 복사해야 한다
RUN chmod +x gradlew

# 루트에서 전체 멀티모듈 빌드
RUN ./gradlew :order-server:bootJar --no-daemon -x test

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# 빌드된 모듈 JAR만 복사
COPY --from=builder /app/order-server/build/libs/*.jar app.jar

ENTRYPOINT ["java","-jar","/app/app.jar"]