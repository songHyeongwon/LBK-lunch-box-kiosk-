# LBK-lunch-box-kiosk-
LBK(lunch box kiosk)

# React, Spring Boot, Redis Docker Compose Setup

## 1. Dockerfile 작성

### (1) React 프론트엔드 Dockerfile

```dockerfile
# frontend/Dockerfile

# Stage 1: Build React app
FROM node:16 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

# Stage 2: Serve with Nginx
FROM nginx:alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

### (2) Spring Boot 백엔드 Dockerfile

```dockerfile
# backend/Dockerfile

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
```

### (3) Redis는 공식 이미지 사용
Redis는 Docker Hub의 공식 이미지를 사용하며 별도의 Dockerfile이 필요 없습니다.

---

## 2. Docker Compose 파일 작성

```yaml
# docker-compose.yml

version: '3.8'

services:
  frontend:
    build:
      context: ./frontend
      dockerfile: Dockerfile
    ports:
      - "3000:80" # React는 기본적으로 Nginx의 80번 포트로 서빙
    depends_on:
      - backend

  backend:
    build:
      context: ./backend
      dockerfile: Dockerfile
    ports:
      - "8080:8080"
    environment:
      - SPRING_REDIS_HOST=redis
      - SPRING_REDIS_PORT=6379
    depends_on:
      - redis

  redis:
    image: redis:alpine
    ports:
      - "6379:6379"
```

---

## 3. Spring Boot 설정

Spring Boot에서 Redis 연결 설정을 `application.yml`에 추가합니다.

```yaml
# backend/src/main/resources/application.yml

spring:
  redis:
    host: ${SPRING_REDIS_HOST:localhost}
    port: ${SPRING_REDIS_PORT:6379}
```

---

## 4. 프로젝트 구조

```
project-root/
├── frontend/
│   ├── Dockerfile
│   ├── package.json
│   ├── src/
│   └── build/
├── backend/
│   ├── Dockerfile
│   ├── src/
│   └── target/
├── docker-compose.yml
└── README.md
```

---

## 5. 실행

1. `docker-compose.yml`이 있는 디렉토리에서 다음 명령어를 실행합니다:
   ```bash
   docker-compose up --build
   ```

2. 브라우저에서 다음 URL을 확인합니다:
   - React 프론트엔드: [http://localhost:3000](http://localhost:3000)
   - Spring Boot 백엔드: [http://localhost:8080](http://localhost:8080)

---

## 6. 추가 사항

- **로그 관리**: 컨테이너별 로그는 `docker logs` 명령을 사용하거나 로깅 드라이버를 설정하여 모니터링합니다.
- **환경 변수 파일**: 민감한 데이터는 `.env` 파일을 통해 관리할 수 있습니다.
- **데이터 볼륨**: Redis 데이터는 휘발성이므로 데이터 볼륨을 추가하여 영속성을 유지할 수 있습니다. 
