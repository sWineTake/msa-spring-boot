# MSA Spring Boot

Spring Boot 기반 마이크로서비스 아키텍처(MSA) 학습 프로젝트

## 기술 스택

- Java 21
- Spring Boot 4.0.1
- Spring Data JPA
- Spring Kafka
- MySQL
- Apache Kafka
- Lombok
- Gradle (Multi-module)

## 프로젝트 구조

```
msa-springboot/
├── build.gradle          # 루트 빌드 설정
├── settings.gradle       # 멀티 모듈 설정
├── user-service/         # 사용자 서비스
│   ├── build.gradle
│   └── src/
├── board-service/        # 게시판 서비스
│   ├── build.gradle
│   └── src/
└── point-service/        # 포인트 서비스
    ├── build.gradle
    └── src/
```

## 서비스 구성

| 서비스 | 포트 | 데이터베이스 | DB 포트 |
|--------|------|--------------|---------|
| user-service | 8080 | user-db | 3306 |
| board-service | 8081 | board-db | 3307 |
| point-service | 8082 | point-db | 3308 |

## 서비스 간 통신

### 동기 통신 (REST API)

```
┌─────────────────┐     회원가입 시 포인트 적립 (REST)      ┌─────────────────┐
│  user-service   │ ──────────────────────────────────▶ │  point-service  │
└─────────────────┘                                     └─────────────────┘
                                                                ▲
┌─────────────────┐     게시글 작성 시 포인트 차감 (REST)           │
│  board-service  │ ────────────────────────────────────────────┘
└─────────────────┘
```

### 비동기 통신 (Kafka)

```
┌─────────────────┐                              ┌─────────────────┐
│  board-service  │ ── board.created Topic ──▶  │  user-service   │
│   (Producer)    │                              │   (Consumer)    │
└─────────────────┘                              └─────────────────┘
        │                                                │
        │ 게시글 작성 완료 이벤트 발행                        │ 활동 점수 10점 추가
        ▼                                                ▼
   BoardCreatedEvent                              addActivityScore()
```

## Kafka Topics

| Topic | Producer | Consumer | 설명 |
|-------|----------|----------|------|
| board.created | board-service | user-service | 게시글 작성 완료 이벤트 |

## 주요 기능

### User Service (포트: 8080)
- 회원가입 / 로그인
- 사용자 조회
- 활동 점수 관리
- Kafka Consumer: 게시글 작성 이벤트 수신 후 활동 점수 추가

### Board Service (포트: 8081)
- 게시글 CRUD
- Kafka Producer: 게시글 작성 완료 이벤트 발행
- Saga 패턴: 게시글 작성 실패 시 보상 트랜잭션 처리

### Point Service (포트: 8082)
- 포인트 적립 / 차감
- 포인트 조회

## Saga 패턴

게시글 작성 시 Saga 패턴을 적용하여 분산 트랜잭션을 처리합니다.

```
게시글 작성 Flow:
1. 포인트 100점 차감 (point-service)
2. 게시글 저장 (board-service)
3. 활동 점수 10점 추가 이벤트 발행 (Kafka)

실패 시 보상 트랜잭션:
- 게시글 저장 실패 → 차감한 포인트 복구
- 포인트 차감 실패 → 게시글 삭제
```

## 실행 방법

### 1. Kafka 실행

```bash
# Kafka 실행 (Zookeeper 포함)
# Docker 사용 시
docker run -d --name zookeeper -p 2181:2181 zookeeper:3.8
docker run -d --name kafka -p 9092:9092 \
  -e KAFKA_ZOOKEEPER_CONNECT=host.docker.internal:2181 \
  -e KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 \
  -e KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 \
  confluentinc/cp-kafka:7.5.0
```

### 2. 데이터베이스 설정

각 서비스에 맞는 MySQL 데이터베이스를 생성합니다.

```sql
-- user-service용
CREATE DATABASE `user-db`;
GRANT ALL PRIVILEGES ON `user-db`.* TO 'admin'@'%';

-- board-service용
CREATE DATABASE `board-db`;
GRANT ALL PRIVILEGES ON `board-db`.* TO 'admin'@'%';

-- point-service용
CREATE DATABASE `point-db`;
GRANT ALL PRIVILEGES ON `point-db`.* TO 'admin'@'%';

FLUSH PRIVILEGES;
```

### 3. 서비스 실행

```bash
# 각 서비스 디렉토리에서 실행
./gradlew :user-service:bootRun
./gradlew :board-service:bootRun
./gradlew :point-service:bootRun
```

## API 예시

### 회원가입
```bash
curl -X POST http://localhost:8080/users/signup \
  -H "Content-Type: application/json" \
  -d '{"email": "test@test.com", "name": "홍길동", "password": "1234"}'
```

### 게시글 작성
```bash
curl -X POST http://localhost:8081/boards \
  -H "Content-Type: application/json" \
  -d '{"title": "제목", "content": "내용", "userId": 1}'
```

### 포인트 조회
```bash
curl http://localhost:8082/points/1
```