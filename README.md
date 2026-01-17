# MSA Spring Boot

Spring Boot 기반 마이크로서비스 아키텍처(MSA) 학습 프로젝트

## 기술 스택

- Java 21
- Spring Boot 4.0.1
- Spring Data JPA
- MySQL
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

```
┌─────────────────┐     회원가입 시 포인트 적립      ┌─────────────────┐
│  user-service   │ ──────────────────────────────▶ │  point-service  │
└─────────────────┘                                 └─────────────────┘
                                                            ▲
┌─────────────────┐     게시글 작성 시 포인트 차감          │
│  board-service  │ ────────────────────────────────────────┘
└─────────────────┘
        │
        │ 게시글 작성 시 활동 점수 추가
        ▼
┌─────────────────┐
│  user-service   │
└─────────────────┘
```

## 실행 방법

### 1. 데이터베이스 설정

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
