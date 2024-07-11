FROM openjdk:17

COPY build/libs/*.jar app.jar

# 빌드된 JAR 파일 실행
ENTRYPOINT ["java", "-jar", "app.jar"]