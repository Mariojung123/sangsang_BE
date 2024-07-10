# 베이스 이미지를 설정합니다. (Node.js 포함)
FROM node:18

# Git을 설치합니다.
RUN apt-get update && apt-get install -y git

# Yarn을 강제로 설치합니다.
RUN npm install -g yarn --force

# Angular CLI를 설치합니다.
RUN npm install -g @angular/cli

# 작업 디렉토리를 설정합니다.
WORKDIR /usr/src/app

# Prompt_angular 폴더를 컨테이너로 복사합니다.
COPY prompt_angular/ ./prompt_angular

# 작업 디렉토리를 설정합니다.
WORKDIR /usr/src/app/prompt_angular

# 의존성을 설치합니다.
RUN yarn install

# 컨테이너에서 사용할 포트를 노출합니다.
EXPOSE 4200

# 기본 명령을 설정합니다.
CMD ["sh", "-c", "yarn && ng serve --host 0.0.0.0"]


