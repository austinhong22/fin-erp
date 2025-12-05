# 📚 KOSA ERP 시스템 코드 이해 가이드

## 🎯 목차
1. [프로젝트 개요](#프로젝트-개요)
2. [아키텍처 구조](#아키텍처-구조)
3. [계층별 역할과 책임](#계층별-역할과-책임)
4. [데이터 흐름](#데이터-흐름)
5. [주요 기능별 동작 원리](#주요-기능별-동작-원리)
6. [핵심 개념 설명](#핵심-개념-설명)
7. [코드 설계 이유](#코드-설계-이유)

---

## 📋 프로젝트 개요

### 무엇을 만든 프로그램인가?
- **회계 ERP 시스템**: 기업의 재무 회계를 관리하는 미니 ERP 프로그램
- **복식부기 원칙**: 차변과 대변의 합계가 반드시 일치해야 하는 회계 원칙을 구현
- **기초정보 관리**: 부서, 계정과목, 거래처, 은행계좌, 예산 등 회계의 기초가 되는 정보 관리
- **전표 입력**: 실제 회계 거래를 전표 형태로 입력하고 저장
- **리포트 생성**: 예산 대비 실적, 거래처 원장 등 회계 리포트 생성

### 기술 스택
- **Java**: 객체지향 프로그래밍 언어
- **JDBC**: Java Database Connectivity (데이터베이스 연결)
- **MySQL**: 관계형 데이터베이스
- **Gradle**: 빌드 도구

---

## 🏗️ 아키텍처 구조

### 전체 구조도
```
┌─────────────────────────────────────────────────────────┐
│                    Main.java                            │
│              (프로그램 진입점, 메뉴 라우팅)                │
└─────────────────────────────────────────────────────────┘
                          │
        ┌─────────────────┼─────────────────┐
        │                 │                 │
        ▼                 ▼                 ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│ Controller   │  │ Controller   │  │ Controller   │
│ (View 계층)  │  │ (View 계층)  │  │ (View 계층)  │
│              │  │              │  │              │
│ - Journal    │  │ - Finance    │  │ - Party      │
│ - Budget     │  │ - Department │  │              │
└──────┬───────┘  └──────┬───────┘  └──────┬───────┘
       │                 │                 │
       ▼                 ▼                 ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│  Service     │  │  Service     │  │  Service     │
│ (비즈니스 로직)│  │ (비즈니스 로직)│  │ (비즈니스 로직)│
│              │  │              │  │              │
│ - Journal    │  │ - Finance    │  │ - Party      │
│ - Budget     │  │ - Department │  │              │
└──────┬───────┘  └──────┬───────┘  └──────┬───────┘
       │                 │                 │
       ▼                 ▼                 ▼
┌──────────────┐  ┌──────────────┐  ┌──────────────┐
│     DAO      │  │     DAO      │  │     DAO      │
│ (데이터 접근) │  │ (데이터 접근) │  │ (데이터 접근) │
│              │  │              │  │              │
│ - Journal    │  │ - Account    │  │ - Party      │
│              │  │ - Budget     │  │ - Department │
└──────┬───────┘  └──────┬───────┘  └──────┬───────┘
       │                 │                 │
       └─────────────────┼─────────────────┘
                         ▼
              ┌──────────────────┐
              │   DBUtil.java    │
              │  (DB 연결 관리)   │
              └────────┬─────────┘
                       ▼
              ┌──────────────────┐
              │   MySQL DB       │
              └──────────────────┘
```

### 패키지 구조
```
org.example/
├── config/          # 설정 클래스
│   ├── AppConfig.java    (회사 ID 등 상수)
│   └── DBUtil.java       (DB 연결 유틸리티)
│
├── dto/             # Data Transfer Object (데이터 전송 객체)
│   ├── AccountDTO.java
│   ├── JournalEntryDTO.java
│   ├── JournalLineDTO.java
│   └── ...
│
├── dao/             # Data Access Object (데이터 접근 계층)
│   ├── AccountDAO.java
│   ├── JournalDAO.java
│   └── ...
│
├── service/         # 비즈니스 로직 계층
│   ├── JournalService.java
│   ├── FinanceService.java
│   └── ...
│
├── view/            # 사용자 인터페이스 계층 (Controller)
│   ├── JournalController.java
│   ├── FinanceController.java
│   └── ...
│
└── Main.java        # 프로그램 진입점
```

---

## 🎭 계층별 역할과 책임

### 1️⃣ View 계층 (Controller)
**위치**: `org.example.view.*`

**역할**:
- 사용자와의 상호작용 담당 (입력/출력)
- 메뉴 표시 및 사용자 입력 받기
- 입력값 검증 (기본적인 형식 검증)
- Service 계층 호출
- 결과를 사용자에게 표시

**예시 코드 흐름**:
```java
// JournalController.java
public void registerJournal() {
    // 1. 사용자로부터 입력 받기
    System.out.print("전표일자: ");
    entry.setEntryDate(sc.nextLine());
    
    // 2. Service 호출
    journalService.registerJournal(entry);
    
    // 3. 결과 출력
    System.out.println("✅ 전표 등록 완료");
}
```

**왜 이렇게 설계했나?**
- **관심사의 분리**: UI 로직과 비즈니스 로직을 분리하여 유지보수성 향상
- **재사용성**: Service는 다른 UI(웹, 모바일)에서도 재사용 가능
- **테스트 용이성**: Service만 테스트하면 비즈니스 로직 검증 가능

---

### 2️⃣ Service 계층 (비즈니스 로직)
**위치**: `org.example.service.*`

**역할**:
- **비즈니스 규칙 검증**: 회계 원칙, 데이터 유효성 검증
- **트랜잭션 관리**: 여러 DB 작업을 하나의 단위로 묶어 처리
- **DAO 조합**: 여러 DAO를 조합하여 복잡한 비즈니스 로직 구현
- **DTO 생성 및 변환**: DAO로부터 받은 데이터를 가공

**핵심 예시: 전표 등록 (복식부기 검증)**
```java
// JournalService.java
public void registerJournal(JournalEntryDTO entry) {
    Connection conn = DBUtil.getConnection();
    conn.setAutoCommit(false);  // 트랜잭션 시작
    
    try {
        // 1. 비즈니스 규칙 검증: 차변/대변 합계 일치 확인
        validateDebitCreditBalance(entry.getLines());
        
        // 2. 전표 헤더 저장
        journalDAO.insertEntry(conn, entry);
        
        // 3. 전표 라인들 저장
        for (JournalLineDTO line : entry.getLines()) {
            journalDAO.insertLine(conn, line);
        }
        
        conn.commit();  // 모든 작업 성공 시 커밋
    } catch (Exception e) {
        conn.rollback();  // 오류 시 롤백
        throw e;
    }
}
```

**왜 이렇게 설계했나?**
- **트랜잭션 보장**: 전표 헤더와 라인은 모두 성공하거나 모두 실패해야 함
- **비즈니스 규칙 집중**: 회계 원칙 검증을 한 곳에서 관리
- **DAO 독립성**: DAO는 단순 CRUD만 담당, 복잡한 로직은 Service에서

---

### 3️⃣ DAO 계층 (데이터 접근)
**위치**: `org.example.dao.*`

**역할**:
- **데이터베이스 직접 접근**: SQL 쿼리 실행
- **CRUD 작업**: Create, Read, Update, Delete
- **ResultSet → DTO 변환**: DB 결과를 Java 객체로 변환
- **Connection 관리**: DBUtil을 통해 Connection 획득 및 반환

**예시 코드**:
```java
// JournalDAO.java
public int insertEntry(Connection conn, JournalEntryDTO entry) {
    String sql = "INSERT INTO journal_entry (id, company_id, entry_date, ...) VALUES (?, ?, ?, ...)";
    
    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, entry.getId());
        pstmt.setString(2, "CP-001");
        // ...
        return pstmt.executeUpdate();
    }
}
```

**왜 이렇게 설계했나?**
- **SQL 분리**: SQL 쿼리를 한 곳에 모아 관리
- **재사용성**: 같은 쿼리를 여러 곳에서 재사용
- **유지보수성**: DB 스키마 변경 시 DAO만 수정하면 됨

---

### 4️⃣ DTO 계층 (데이터 전송 객체)
**위치**: `org.example.dto.*`

**역할**:
- **데이터 캡슐화**: 관련 데이터를 하나의 객체로 묶음
- **계층 간 데이터 전달**: Controller ↔ Service ↔ DAO 간 데이터 전달
- **타입 안정성**: 원시 타입 대신 객체 사용으로 타입 안정성 확보

**예시**:
```java
// JournalEntryDTO.java
public class JournalEntryDTO {
    private String id;
    private String entryDate;
    private String description;
    private List<JournalLineDTO> lines;  // 1:N 관계 표현
    
    public void addLine(JournalLineDTO line) {
        this.lines.add(line);
    }
}
```

**왜 이렇게 설계했나?**
- **데이터 구조 명확화**: 어떤 데이터가 전달되는지 명확히 표현
- **유지보수성**: 필드 추가/변경 시 한 곳만 수정
- **가독성**: 원시 타입보다 의미 있는 객체 사용

---

## 🔄 데이터 흐름

### 시나리오 1: 전표 등록 (가장 복잡한 흐름)

```
[사용자 입력]
    │
    ▼
┌─────────────────────────────────────────┐
│  JournalController.registerJournal()    │
│  - 사용자로부터 전표 정보 입력 받기        │
│  - JournalEntryDTO 객체 생성 및 데이터 설정│
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  JournalService.registerJournal()       │
│  - 트랜잭션 시작 (conn.setAutoCommit(false))│
│  - 차변/대변 합계 검증                    │
│  - UUID 생성 (전표 ID)                   │
└──────────────┬──────────────────────────┘
               │
       ┌───────┴───────┐
       │               │
       ▼               ▼
┌─────────────┐  ┌─────────────┐
│JournalDAO   │  │JournalDAO   │
│insertEntry()│  │insertLine() │
│(헤더 저장)   │  │(라인 저장)   │
└──────┬──────┘  └──────┬──────┘
       │               │
       └───────┬───────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  MySQL Database                         │
│  - journal_entry 테이블에 INSERT        │
│  - journal_line 테이블에 INSERT (여러 개)│
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  성공 시: conn.commit()                  │
│  실패 시: conn.rollback()                │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  JournalController                      │
│  - "✅ 전표 등록 완료" 메시지 출력        │
└─────────────────────────────────────────┘
```

### 시나리오 2: 예산 대비 실적 리포트 조회

```
[사용자 입력: 연월 "202501"]
    │
    ▼
┌─────────────────────────────────────────┐
│  FinanceController.showBudgetReport()   │
│  - 사용자로부터 연월 입력 받기            │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  FinanceService.showBudgetReport()       │
│  - ReportDAO 호출                       │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  ReportDAO.selectBudgetReport()         │
│  - 복잡한 JOIN 쿼리 실행                  │
│    (budget + department + gl_account    │
│     + journal_line + journal_entry)      │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  MySQL Database                         │
│  - LEFT JOIN으로 예산은 있지만 지출이     │
│    없는 항목도 포함하여 조회              │
│  - GROUP BY로 부서별, 계정별 집계         │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  ReportDTO 리스트 반환                   │
│  (부서명, 계정명, 예산, 지출, 잔액)       │
└──────────────┬──────────────────────────┘
               │
               ▼
┌─────────────────────────────────────────┐
│  FinanceController                      │
│  - 표 형태로 리포트 출력                 │
└─────────────────────────────────────────┘
```

---

## 🔧 주요 기능별 동작 원리

### 1. 전표 입력 (복식부기 검증)

**핵심 개념**: 복식부기 원칙
- 모든 거래는 **차변(Debit)**과 **대변(Credit)**으로 나뉨
- **차변 합계 = 대변 합계**가 반드시 일치해야 함

**코드 흐름**:

```12:95:src/main/java/org/example/service/JournalService.java
    private void validateDebitCreditBalance(List<JournalLineDTO> lines) {
        BigDecimal debitSum = BigDecimal.ZERO;
        BigDecimal creditSum = BigDecimal.ZERO;

        for (JournalLineDTO line : lines) {
            if (line.getDebitAmount() != null) {
                debitSum = debitSum.add(line.getDebitAmount());
            }
            if (line.getCreditAmount() != null) {
                creditSum = creditSum.add(line.getCreditAmount());
            }
        }

        if (debitSum.compareTo(creditSum) != 0) {
            throw new IllegalArgumentException(
                    String.format("차변/대변 불일치: 차변 합계=%s, 대변 합계=%s", debitSum, creditSum));
        }
    }
```

**왜 BigDecimal을 사용하나?**
- **정밀도**: 돈 계산은 정확해야 하므로 부동소수점 오류를 피하기 위해 BigDecimal 사용
- **금융 표준**: Java에서 금융 계산의 표준 타입

**트랜잭션 처리**:
```19:70:src/main/java/org/example/service/JournalService.java
    public void registerJournal(JournalEntryDTO entry) throws SQLException {
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 1. 차변/대변 합계 검증
            validateDebitCreditBalance(entry.getLines());

            // 2. 전표 헤더 INSERT
            if (entry.getId() == null || entry.getId().isEmpty()) {
                entry.setId(UUID.randomUUID().toString());
            }
            int entryResult = journalDAO.insertEntry(conn, entry);
            if (entryResult <= 0) {
                throw new SQLException("전표 헤더 저장 실패");
            }

            // 3. 전표 라인들 INSERT
            List<JournalLineDTO> lines = entry.getLines();
            for (JournalLineDTO line : lines) {
                if (line.getId() == null || line.getId().isEmpty()) {
                    line.setId(UUID.randomUUID().toString());
                }
                line.setJournalEntryId(entry.getId()); // 헤더 ID 연결

                int lineResult = journalDAO.insertLine(conn, line);
                if (lineResult <= 0) {
                    throw new SQLException("전표 라인 저장 실패: " + line);
                }
            }

            // 4. 모든 작업 성공 시 커밋
            conn.commit();
            System.out.println("✅ 전표 등록 완료: " + entry.getId());

        } catch (Exception e) {
            // 5. 오류 발생 시 롤백
            if (conn != null) {
                conn.rollback();
                System.out.println("❌ 전표 등록 실패: 롤백 처리됨");
            }
            throw e;
        } finally {
            // 6. Connection 정리
            if (conn != null) {
                conn.setAutoCommit(true); // 원래대로 복구
                conn.close();
            }
        }
    }
```

**왜 트랜잭션을 사용하나?**
- **원자성 보장**: 전표 헤더와 모든 라인이 모두 저장되거나 모두 실패해야 함
- **데이터 일관성**: 중간에 오류 발생 시 이전 작업도 취소되어 불완전한 데이터 방지

---

### 2. Soft Delete (논리 삭제)

**핵심 개념**: 실제로 데이터를 삭제하지 않고 `is_active` 플래그로 비활성화

**왜 Soft Delete를 사용하나?**
- **데이터 보존**: 과거 거래 내역은 삭제하면 안 됨 (회계 감사 필요)
- **복구 가능**: 실수로 삭제한 경우 복구 가능
- **참조 무결성**: 다른 테이블에서 참조하는 데이터는 삭제하면 안 됨

**구현 방식**:

```30:40:src/main/java/org/example/dao/AccountDAO.java
    // 2. Soft Delete 구현 (DELETE 대신 UPDATE)
    public int softDeleteById(String id) throws SQLException {
        String sql = "UPDATE gl_account SET is_active = 'N' WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            return pstmt.executeUpdate();
        }
    }
```

**조회 시 필터링**:

```55:76:src/main/java/org/example/dao/AccountDAO.java
    // 4. 전체 조회 (활성 필터링)
    public List<AccountDTO> findAll() throws SQLException {
        String sql = "SELECT * FROM gl_account WHERE is_active = 'Y'";
        List<AccountDTO> accountInfo = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                AccountDTO dto = new AccountDTO();
                dto.setId(rs.getString("id"));
                dto.setCompanyId(rs.getString("company_id"));
                dto.setCode(rs.getString("code"));
                dto.setName(rs.getString("name"));
                dto.setType(AccountType.valueOf(rs.getString("type")));

                accountInfo.add(dto);
            }
            return accountInfo;
        }
    }
```

**복구 기능**:

```42:52:src/main/java/org/example/dao/AccountDAO.java
    // 3. RESTORE (복구) 기능 추가
    public int restoreById(String id) throws SQLException {
        String sql = "UPDATE gl_account SET is_active = 'Y' WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            return pstmt.executeUpdate();
        }
    }
```

---

### 3. 예산 대비 실적 리포트

**핵심 개념**: LEFT JOIN을 사용하여 예산은 있지만 지출이 없는 항목도 포함

**SQL 쿼리 분석**:

```18:37:src/main/java/org/example/dao/ReportDAO.java
        String sql = """
                SELECT
                    d.name  AS dept,
                    ga.name AS account,
                    b.budget_amount AS budget,
                    COALESCE(SUM(jl.debit_amount), 0) AS expense,
                    (b.budget_amount - COALESCE(SUM(jl.debit_amount), 0)) AS balance
                FROM budget b
                JOIN department d ON b.department_id = d.id
                JOIN gl_account ga ON b.gl_account_id = ga.id
                LEFT JOIN journal_line jl
                    ON jl.department_id = b.department_id
                   AND jl.gl_account_id = b.gl_account_id
                LEFT JOIN journal_entry je
                    ON je.id = jl.journal_entry_id
                   AND DATE_FORMAT(STR_TO_DATE(je.entry_date, '%Y-%m-%d'), '%Y%m') = ?
                WHERE b.year_month = ?
                GROUP BY d.name, ga.name, b.budget_amount
                ORDER BY d.name, ga.name
                """;
```

**왜 LEFT JOIN을 사용하나?**
- **예산은 있지만 지출이 없는 경우**: 예산은 등록했지만 아직 지출이 없는 항목도 리포트에 표시해야 함
- **INNER JOIN이면**: 지출이 있는 항목만 표시됨 (예산만 있고 지출이 없는 항목은 누락)

**COALESCE 함수**:
- `COALESCE(SUM(jl.debit_amount), 0)`: 지출이 없으면 NULL 대신 0 반환

---

### 4. 거래처 원장 조회

**핵심 개념**: 특정 거래처의 모든 거래 내역을 시간순으로 조회

**SQL 쿼리**:

```198:237:src/main/java/org/example/dao/PartyDAO.java
    // 거래처 원장 상세 내역 조회 (리포트용)
    public List<PartyLedgerDTO> selectLedgerLinesByPartyId(String partyId) throws SQLException {

        // 과거 거래 내역은 비활성화된 거래처라도 보여줘야 하므로 is_active를 추가하지 않습니다.
        String sql = "SELECT " +
                "  je.entry_date, " +
                "  je.description, " +
                "  gl.name AS gl_account_name, " +
                "  jl.debit_amount, " +
                "  jl.credit_amount, " +
                "  registration_number " +
                "FROM journal_entry je " +
                "JOIN journal_line jl ON je.id = jl.journal_entry_id " +
                "JOIN gl_account gl ON jl.gl_account_id = gl.id " +
                "JOIN party p ON je.party_id = p.id " +
                "WHERE je.party_id = ? " +
                "ORDER BY je.entry_date";

        List<PartyLedgerDTO> ledgerList = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, partyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PartyLedgerDTO dto = new PartyLedgerDTO();
                    dto.setEntryDate(rs.getString("entry_date"));
                    dto.setDescription(rs.getString("description"));
                    dto.setGlAccountName(rs.getString("gl_account_name"));
                    dto.setRegistration(rs.getString("registration_number"));
                    dto.setDebitAmount(rs.getBigDecimal("debit_amount"));
                    dto.setCreditAmount(rs.getBigDecimal("credit_amount"));

                    ledgerList.add(dto);
                }
            }
        }
        return ledgerList;
    }
```

**왜 여러 테이블을 JOIN하나?**
- `journal_entry`: 전표 헤더 (날짜, 설명)
- `journal_line`: 전표 라인 (차변, 대변 금액)
- `gl_account`: 계정과목 이름
- `party`: 거래처 정보 (사업자번호 등)

**과거 거래는 비활성 거래처도 표시**:
- 주석에 명시: "과거 거래 내역은 비활성화된 거래처라도 보여줘야 하므로 is_active를 추가하지 않습니다"
- 이유: 과거 거래 내역은 회계 감사에 필요하므로 비활성화된 거래처의 거래도 조회해야 함

---

## 💡 핵심 개념 설명

### 1. 3-Tier 아키텍처 (3계층 구조)

**계층 구조**:
1. **Presentation Layer (표현 계층)**: Controller - 사용자 인터페이스
2. **Business Logic Layer (비즈니스 로직 계층)**: Service - 비즈니스 규칙
3. **Data Access Layer (데이터 접근 계층)**: DAO - 데이터베이스 접근

**장점**:
- **관심사의 분리**: 각 계층이 명확한 책임을 가짐
- **유지보수성**: 한 계층 수정이 다른 계층에 미치는 영향 최소화
- **재사용성**: Service는 다른 UI에서도 재사용 가능
- **테스트 용이성**: 각 계층을 독립적으로 테스트 가능

---

### 2. DTO (Data Transfer Object)

**역할**: 계층 간 데이터 전달을 위한 객체

**예시**:
```java
// JournalEntryDTO: 전표 헤더 정보
public class JournalEntryDTO {
    private String id;
    private String entryDate;
    private String description;
    private String partyId;
    private String bankAccountId;
    private List<JournalLineDTO> lines;  // 1:N 관계
}
```

**왜 사용하나?**
- **타입 안정성**: 원시 타입 대신 의미 있는 객체 사용
- **데이터 캡슐화**: 관련 데이터를 하나로 묶음
- **유지보수성**: 필드 추가/변경 시 한 곳만 수정

---

### 3. 트랜잭션 (Transaction)

**정의**: 여러 DB 작업을 하나의 단위로 묶어서 모두 성공하거나 모두 실패하도록 보장

**ACID 원칙**:
- **Atomicity (원자성)**: 모두 성공하거나 모두 실패
- **Consistency (일관성)**: 데이터 일관성 유지
- **Isolation (격리성)**: 동시 실행 시 서로 간섭 없음
- **Durability (지속성)**: 커밋된 데이터는 영구 저장

**코드에서의 구현**:
```java
conn.setAutoCommit(false);  // 자동 커밋 비활성화
try {
    // 여러 작업 수행
    dao1.insert(...);
    dao2.insert(...);
    conn.commit();  // 모두 성공 시 커밋
} catch (Exception e) {
    conn.rollback();  // 실패 시 롤백
}
```

---

### 4. PreparedStatement

**역의**: SQL 인젝션 공격 방지 및 성능 향상

**사용 예시**:
```java
String sql = "SELECT * FROM party WHERE id = ?";
PreparedStatement pstmt = conn.prepareStatement(sql);
pstmt.setString(1, partyId);  // ?에 값 바인딩
ResultSet rs = pstmt.executeQuery();
```

**장점**:
- **SQL 인젝션 방지**: 사용자 입력을 안전하게 처리
- **성능 향상**: 쿼리 재사용 가능
- **타입 안정성**: 타입 체크 가능

---

### 5. UUID (Universally Unique Identifier)

**사용 이유**: 고유한 ID 생성

**코드에서의 사용**:
```java
entry.setId(UUID.randomUUID().toString());
// 예: "550e8400-e29b-41d4-a716-446655440000"
```

**장점**:
- **고유성 보장**: 중복 가능성 거의 없음
- **분산 환경**: 여러 서버에서도 중복 없이 생성 가능
- **보안성**: 순차적 ID보다 예측하기 어려움

**단점**:
- **길이**: 36자로 길어서 가독성 떨어짐
- **인덱스 성능**: 문자열이라 숫자보다 느릴 수 있음

---

## 🎨 코드 설계 이유

### 1. 왜 계층을 나눴나?

**단일 파일에 모든 코드를 넣으면**:
- 코드가 길어져서 이해하기 어려움
- 수정 시 다른 기능에 영향
- 테스트하기 어려움

**계층 분리의 효과**:
- 각 계층의 역할이 명확함
- 수정 범위가 명확함
- 독립적으로 테스트 가능

---

### 2. 왜 Service에서 트랜잭션을 관리하나?

**DAO에서 트랜잭션을 관리하면**:
- 여러 DAO를 조합할 때 트랜잭션 관리가 어려움
- 비즈니스 로직이 분산됨

**Service에서 관리하면**:
- 여러 DAO 작업을 하나의 트랜잭션으로 묶을 수 있음
- 비즈니스 로직이 한 곳에 집중됨

**예시**:
```java
// Service에서 여러 DAO 작업을 하나의 트랜잭션으로
conn.setAutoCommit(false);
journalDAO.insertEntry(conn, entry);      // DAO 1
journalDAO.insertLine(conn, line1);       // DAO 2
journalDAO.insertLine(conn, line2);       // DAO 3
conn.commit();  // 모두 성공 시 커밋
```

---

### 3. 왜 DTO를 사용하나?

**원시 타입을 직접 전달하면**:
```java
// 나쁜 예
public void registerJournal(String entryDate, String description, 
                           String partyId, String bankAccountId, ...) {
    // 파라미터가 너무 많아서 관리 어려움
}
```

**DTO 사용**:
```java
// 좋은 예
public void registerJournal(JournalEntryDTO entry) {
    // 관련 데이터가 하나의 객체로 묶여 있음
}
```

**장점**:
- 파라미터 개수 감소
- 관련 데이터 그룹화
- 필드 추가 시 시그니처 변경 없음

---

### 4. 왜 Soft Delete를 사용하나?

**Hard Delete (실제 삭제)의 문제점**:
- 과거 거래 내역이 사라져서 회계 감사 불가
- 실수로 삭제하면 복구 불가
- 다른 테이블에서 참조하는 경우 삭제 불가

**Soft Delete의 장점**:
- 데이터 보존 (회계 감사 가능)
- 복구 가능
- 참조 무결성 유지

**구현**:
```sql
-- 삭제 대신
UPDATE party SET is_active = 'N' WHERE id = ?

-- 조회 시 필터링
SELECT * FROM party WHERE is_active = 'Y'
```

---

### 5. 왜 Connection을 Service에서 관리하나?

**DAO에서 Connection을 관리하면**:
- 여러 DAO 작업을 하나의 트랜잭션으로 묶을 수 없음
- 각 DAO가 독립적인 트랜잭션을 가짐

**Service에서 관리하면**:
- 여러 DAO에 같은 Connection을 전달하여 하나의 트랜잭션으로 처리
- 원자성 보장

**코드 예시**:
```java
// Service
Connection conn = DBUtil.getConnection();
conn.setAutoCommit(false);

// 같은 Connection을 여러 DAO에 전달
journalDAO.insertEntry(conn, entry);
journalDAO.insertLine(conn, line1);
journalDAO.insertLine(conn, line2);

conn.commit();  // 모두 성공 시 커밋
```

---

## 📊 데이터베이스 스키마 이해

### 주요 테이블 관계

```
company (회사)
    │
    ├── department (부서)
    │       │
    │       └── budget (예산) ── gl_account (계정과목)
    │
    ├── gl_account (계정과목)
    │
    ├── party (거래처)
    │
    ├── bank_account (은행계좌)
    │
    └── journal_entry (전표 헤더)
            │
            ├── journal_line (전표 라인) ── gl_account
            │                           └── department
            │
            ├── party (거래처)
            └── bank_account (은행계좌)
```

### 외래키 관계

- `department.company_id` → `company.id`
- `gl_account.company_id` → `company.id`
- `party.company_id` → `company.id`
- `budget.department_id` → `department.id`
- `budget.gl_account_id` → `gl_account.id`
- `journal_entry.party_id` → `party.id`
- `journal_line.journal_entry_id` → `journal_entry.id` (ON DELETE CASCADE)

**ON DELETE CASCADE 의미**:
- 전표 헤더가 삭제되면 관련 라인도 자동 삭제
- 데이터 일관성 유지

---

## 🔍 코드 읽기 팁

### 1. Main.java부터 시작
- 프로그램 진입점
- 전체 메뉴 구조 파악
- 어떤 Controller가 있는지 확인

### 2. Controller → Service → DAO 순서로 읽기
- 사용자 요청이 어떻게 처리되는지 추적
- 데이터 흐름 이해

### 3. DTO 구조 먼저 파악
- 어떤 데이터가 전달되는지 확인
- 테이블 구조와 비교

### 4. SQL 쿼리 이해
- JOIN 관계 파악
- WHERE 조건 확인
- GROUP BY, ORDER BY 이해

---

## 🎓 학습 체크리스트

### 기본 개념 이해
- [ ] 3-Tier 아키텍처 이해
- [ ] DTO, DAO, Service, Controller 역할 구분
- [ ] 트랜잭션 개념 이해
- [ ] Soft Delete 개념 이해

### 코드 흐름 이해
- [ ] 전표 등록 전체 흐름 추적
- [ ] 예산 리포트 조회 흐름 추적
- [ ] 거래처 원장 조회 흐름 추적

### 데이터베이스 이해
- [ ] 주요 테이블 구조 파악
- [ ] 외래키 관계 이해
- [ ] JOIN 쿼리 이해

### 설계 원칙 이해
- [ ] 왜 계층을 나눴는지
- [ ] 왜 트랜잭션을 사용하는지
- [ ] 왜 Soft Delete를 사용하는지

---

## 📝 마무리

이 문서는 KOSA ERP 시스템의 코드를 이해하기 위한 가이드입니다. 

**다음 단계**:
1. 코드를 직접 실행해보기
2. 각 기능을 하나씩 테스트해보기
3. 코드를 수정해보며 동작 확인하기
4. 새로운 기능 추가해보기

**질문이 있으면**:
- 코드 주석 확인
- TEST_SCENARIO.md 참고
- 팀원들과 토론

**기억할 점**:
- 코드는 도구일 뿐, **이해하는 것이 중요**
- 처음엔 어려워도 **반복 학습하면 이해됨**
- **실습**이 가장 좋은 학습 방법

---

**작성일**: 2025년
**버전**: 1.0



