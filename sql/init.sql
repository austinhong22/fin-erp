
-- 1. DB 초기화 (기존 데이터 삭제)
DROP DATABASE IF EXISTS minierp_level2;
CREATE DATABASE minierp_level2 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE minierp_level2;

-- ==========================================
-- 2. 테이블 생성 (Table DDL)
-- ==========================================

-- [1] 회사 (최상위 개념)
CREATE TABLE company (
                         id VARCHAR(36) PRIMARY KEY COMMENT 'UUID',
                         name VARCHAR(100) NOT NULL,
                         business_no VARCHAR(50),
                         owner_name VARCHAR(50),
                         created_at DATETIME DEFAULT CURRENT_TIMESTAMP
);

-- [2] 부서 (조직)
CREATE TABLE department (
                            id VARCHAR(36) PRIMARY KEY COMMENT 'UUID',
                            company_id VARCHAR(36) NOT NULL,
                            name VARCHAR(100) NOT NULL,
                            code VARCHAR(50) NOT NULL COMMENT '부서코드 (DEV, SALES)',
                            FOREIGN KEY (company_id) REFERENCES company(id)
);

-- [3] 계정과목 (회계항목)
CREATE TABLE gl_account (
                            id VARCHAR(36) PRIMARY KEY COMMENT 'UUID',
                            company_id VARCHAR(36) NOT NULL,
                            code VARCHAR(20) NOT NULL COMMENT '계정코드 (811, 401)',
                            name VARCHAR(100) NOT NULL COMMENT '계정명 (복리후생비, 매출)',
                            type VARCHAR(20) NOT NULL COMMENT 'ASSET, LIABILITY, EQUITY, REVENUE, EXPENSE',
                            FOREIGN KEY (company_id) REFERENCES company(id)
);

-- [4] 거래처 (고객/공급사) - Level 2 핵심
CREATE TABLE party (
                       id VARCHAR(36) PRIMARY KEY COMMENT 'UUID',
                       company_id VARCHAR(36) NOT NULL,
                       name VARCHAR(100) NOT NULL COMMENT '거래처명',
                       type VARCHAR(20) NOT NULL COMMENT 'CUSTOMER(고객), VENDOR(공급사)',
                       contact VARCHAR(100),
                       registration_number VARCHAR(50),
                       FOREIGN KEY (company_id) REFERENCES company(id)
);

-- [5] 은행 계좌 (자금)
CREATE TABLE bank_account (
                              id VARCHAR(36) PRIMARY KEY COMMENT 'UUID',
                              company_id VARCHAR(36) NOT NULL,
                              bank_name VARCHAR(100) NOT NULL,
                              account_no VARCHAR(50) NOT NULL,
                              account_alias VARCHAR(100) COMMENT '계좌별칭 (메인통장)',
                              description VARCHAR(255),
                              FOREIGN KEY (company_id) REFERENCES company(id)
);

-- [6] 예산 (Budget) - Level 2 핵심
CREATE TABLE budget (
                        id VARCHAR(36) PRIMARY KEY COMMENT 'UUID',
                        company_id VARCHAR(36) NOT NULL,
                        department_id VARCHAR(36) NOT NULL,
                        gl_account_id VARCHAR(36) NOT NULL,
                        year_month CHAR(6) NOT NULL COMMENT 'YYYYMM (예: 202501)',
                        budget_amount DECIMAL(19, 0) DEFAULT 0 COMMENT '예산금액',

                        FOREIGN KEY (company_id) REFERENCES company(id),
                        FOREIGN KEY (department_id) REFERENCES department(id),
                        FOREIGN KEY (gl_account_id) REFERENCES gl_account(id),

    -- [중요] 한 부서가 같은 달, 같은 계정에 중복 예산 등록 방지
                        UNIQUE KEY uk_budget_dept_acc_month (department_id, gl_account_id, year_month)
);

-- [7] 전표 헤더 (Journal Entry) - 트랜잭션의 시작
CREATE TABLE journal_entry (
                               id VARCHAR(36) PRIMARY KEY COMMENT 'UUID',
                               company_id VARCHAR(36) NOT NULL,
                               entry_date DATE NOT NULL COMMENT '거래일자',
                               description VARCHAR(255) COMMENT '적요',

    -- 선택 입력 정보 (NULL 가능)
                               party_id VARCHAR(36) COMMENT '관련 거래처',
                               bank_account_id VARCHAR(36) COMMENT '관련 계좌',

                               created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                               created_by VARCHAR(50) DEFAULT 'SYSTEM',

                               FOREIGN KEY (company_id) REFERENCES company(id),
                               FOREIGN KEY (party_id) REFERENCES party(id),
                               FOREIGN KEY (bank_account_id) REFERENCES bank_account(id)
);

-- [8] 전표 라인 (Journal Line) - 상세 내역
CREATE TABLE journal_line (
                              id VARCHAR(36) PRIMARY KEY COMMENT 'UUID',
                              journal_entry_id VARCHAR(36) NOT NULL,
                              gl_account_id VARCHAR(36) NOT NULL,
                              department_id VARCHAR(36) COMMENT '비용 귀속 부서',

                              debit_amount DECIMAL(19, 0) DEFAULT 0 COMMENT '차변 금액',
                              credit_amount DECIMAL(19, 0) DEFAULT 0 COMMENT '대변 금액',
                              memo VARCHAR(255) COMMENT '라인 메모',

                              FOREIGN KEY (journal_entry_id) REFERENCES journal_entry(id) ON DELETE CASCADE,
                              FOREIGN KEY (gl_account_id) REFERENCES gl_account(id),
                              FOREIGN KEY (department_id) REFERENCES department(id)
);


-- ==========================================
-- 3. 기초 데이터 시딩 (Seed Data)
-- 팀원들이 테스트할 때 바로 쓸 수 있는 데이터
-- UUID 대신 읽기 쉬운 문자열 ID 사용 (초기 데이터라 가능)
-- ==========================================

-- (1) 회사 등록
INSERT INTO company (id, name, business_no) VALUES
    ('CP-001', 'Team SIU Corp', '123-45-67890');

-- (2) 부서 등록
INSERT INTO department (id, company_id, name, code) VALUES
                                                        ('DEPT-01', 'CP-001', '개발팀', 'DEV'),
                                                        ('DEPT-02', 'CP-001', '영업팀', 'SALES'),
                                                        ('DEPT-03', 'CP-001', '경영지원팀', 'MGT');

-- (3) 계정과목 등록 (자산, 비용, 수익)
INSERT INTO gl_account (id, company_id, code, name, type) VALUES
                                                              ('ACC-001', 'CP-001', '101', '보통예금', 'ASSET'),
                                                              ('ACC-002', 'CP-001', '401', '용역매출', 'REVENUE'),
                                                              ('ACC-003', 'CP-001', '811', '복리후생비(식대)', 'EXPENSE'),
                                                              ('ACC-004', 'CP-001', '812', '도서인쇄비', 'EXPENSE'),
                                                              ('ACC-005', 'CP-001', '813', '서버유지비', 'EXPENSE');

-- (4) 거래처 등록
INSERT INTO party (id, company_id, name, type, contact) VALUES
                                                            ('PTY-001', 'CP-001', '삼성전자', 'CUSTOMER', '김삼성 010-1111-2222'),
                                                            ('PTY-002', 'CP-001', 'AWS Korea', 'VENDOR', 'cloud@aws.com'),
                                                            ('PTY-003', 'CP-001', '스타벅스', 'VENDOR', '매장결제');

-- (5) 은행 계좌 등록
INSERT INTO bank_account (id, company_id, bank_name, account_no, account_alias, description) VALUES
    ('BANK-001', 'CP-001', '국민은행', '111-222-333333', '메인운영통장', '법인카드 결제 계좌');

-- (6) 예산 등록 (개발팀 2025년 1월 예산)
-- 개발팀 식대 100만원, 서버비 50만원
INSERT INTO budget (id, company_id, department_id, gl_account_id, year_month, budget_amount) VALUES
                                                                                                 ('BUD-001', 'CP-001', 'DEPT-01', 'ACC-003', '202501', 1000000), -- 식대
                                                                                                 ('BUD-002', 'CP-001', 'DEPT-01', 'ACC-005', '202501', 500000);  -- 서버비

