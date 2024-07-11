# SecureAndBox Project


SpringBoot 사용법
- 스프링 부트를 안정적으로 실행은 가능하게 하였으나   저의 능력 부족으로 변경할 때 마다 다시 gradle 빌드하고 도커를 빌드해야 하는 상황입니다...   번거롭더라도 수고 바래요...   
1. backend 파일로 들어간다.
2. ./gradlew build
3. docker-compose up --build

- 2번은 새로운 jar 파일을 만들기 위한 명령어
- 3번은 새로 만든 jar 파일을 도커로 다시 빌드하기
## 만일 Docker-compose up 또는 Docker-compose up --build를 했을 때 오류가 발생한다면??
## -> DB가 켜져있는지 확인 해주세요 스프링 코드에 DB 연결하는 부분 있어서 DB 꺼져있으면 서버가 안켜져요


### http://localhost:8080   
<img width="400" alt="image" src="https://github.com/Mariojung123/KEBProject_BE/assets/115441849/cdc41562-8c10-498f-a72c-aecb3acd5f56">   

### http://localhost:8080/swagger-ui/index.html#/home-controller/testApi   
<img width="400" alt="image" src="https://github.com/Mariojung123/KEBProject_BE/assets/115441849/8d16dcad-26f1-4984-be22-7395e41d7ae0">
