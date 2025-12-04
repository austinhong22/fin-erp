# KOSA ERP

자바 + JDBC로 구현하는 회계 ERP 시스템

## 기술 스택
- Java
- JDBC
- MySQL
- Gradle

## 프로젝트 구조
```
src/main/java/org/example/
├── config/     # DB 설정
├── dao/        # 데이터 접근 계층
├── dto/        # 데이터 전송 객체
├── service/    # 비즈니스 로직
└── view/       # 사용자 인터페이스
```

## 데이터베이스 설정
1. MySQL 데이터베이스 생성
2. `sql/init.sql` 실행하여 테이블 및 초기 데이터 생성

## 실행 방법
```bash
./gradlew build
./gradlew run
```







