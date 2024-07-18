# 기본 Java 이미지를 사용하여 빌드 단계 설정
FROM openjdk:17-jdk-slim as build

# 필요한 패키지 설치
RUN apt-get update && apt-get install -y tzdata

# 시간대 설정
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 애플리케이션의 작업 디렉토리 설정
WORKDIR /app

# gradlew 및 그 외 필요한 파일들을 이미지에 복사
COPY gradlew gradlew.bat gradle /app/
COPY gradle /app/gradle
COPY build.gradle settings.gradle /app/
COPY src /app/src/

# 'gradle/wrapper' 디렉토리에서 필요한 파일 추가
COPY gradle/wrapper /app/gradle/wrapper

# gradlew 실행 권한 부여 및 빌드 수행
RUN chmod +x /app/gradlew
RUN /app/gradlew build -x test

# 최종 이미지 준비
FROM openjdk:17-jdk-slim

# 애플리케이션 실행 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일을 최종 이미지로 복사
COPY --from=build build/libs/*.jar /app/app.jar

# 애플리케이션 실행 명령 설정
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
