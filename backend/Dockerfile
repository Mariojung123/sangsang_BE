FROM openjdk:17

WORKDIR /app

# 필요한 파일들을 복사합니다.
COPY gradlew .
COPY gradle ./gradle
COPY build.gradle .
COPY settings.gradle .
COPY src ./src

# Gradle Wrapper에 실행 권한을 부여합니다.
RUN chmod +x gradlew

RUN microdnf install findutils
# 필요한 유틸리티 설치

# Gradle을 사용하여 애플리케이션을 빌드합니다.
RUN ./gradlew build

VOLUME ["/src/main", "/app/src/main"]

VOLUME ["/src/test", "/app/src/test"]

# 빌드된 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "./build/libs/KguSandBox-0.0.1-SNAPSHOT.jar"]

# 애플리케이션 포트를 노출합니다.
EXPOSE 8080
