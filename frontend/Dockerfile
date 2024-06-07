# Node 이미지 사용
FROM node:14
# 작업 디렉토리 설정
WORKDIR /app
# 앱 소스를 이미지에 복사
COPY . .
# 필요한 패키지 설치
RUN npm install
# VOLUME 설정
VOLUME ["/app/src", "/app/build"]
# 서버 실행
CMD ["npx", "serve", "-s", "build", "-l", "3000"]
