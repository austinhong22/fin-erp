-- 1. DB 초기화
DROP DATABASE IF EXISTS minierp_level2;
CREATE DATABASE minierp_level2 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE minierp_level2;

-- 2. 테이블 생성

CREATE TABLE `company` (
                           `id` VARCHAR(36) PRIMARY KEY,
                           `name` VARCHAR(100) NOT NULL,
                           `business_no` VARCHAR(50),
                           `owner_name` VARCHAR(50),
                           `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `department` (
                              `id` VARCHAR(36) PRIMARY KEY,
                              `company_id` VARCHAR(36) NOT NULL,
                              `name` VARCHAR(100) NOT NULL,
                              `code` VARCHAR(50) NOT NULL,
                              CONSTRAINT `fk_dept_company` FOREIGN KEY (`company_id`) REFERENCES `company`(`id`)
);

CREATE TABLE `gl_account` (
                              `id` VARCHAR(36) PRIMARY KEY,
                              `company_id` VARCHAR(36) NOT NULL,
                              `code` VARCHAR(20) NOT NULL,
                              `name` VARCHAR(100) NOT NULL,
                              `type` VARCHAR(20) NOT NULL,
                              CONSTRAINT `fk_gl_company` FOREIGN KEY (`company_id`) REFERENCES `company`(`id`)
);

CREATE TABLE `party` (
                         `id` VARCHAR(36) PRIMARY KEY,
                         `company_id` VARCHAR(36) NOT NULL,
                         `name` VARCHAR(100) NOT NULL,
                         `type` VARCHAR(20) NOT NULL,
                         `contact` VARCHAR(100),
                         `registration_number` VARCHAR(50),
                         CONSTRAINT `fk_party_company` FOREIGN KEY (`company_id`) REFERENCES `company`(`id`)
);

CREATE TABLE `bank_account` (
                                `id` VARCHAR(36) PRIMARY KEY,
                                `company_id` VARCHAR(36) NOT NULL,
                                `bank_name` VARCHAR(100) NOT NULL,
                                `account_no` VARCHAR(50) NOT NULL,
                                `account_alias` VARCHAR(100),
                                `description` VARCHAR(255),
                                CONSTRAINT `fk_bank_company` FOREIGN KEY (`company_id`) REFERENCES `company`(`id`)
);

CREATE TABLE `budget` (
                          `id` VARCHAR(36) PRIMARY KEY,
                          `company_id` VARCHAR(36) NOT NULL,
                          `department_id` VARCHAR(36) NOT NULL,
                          `gl_account_id` VARCHAR(36) NOT NULL,
                          `year_month` CHAR(6) NOT NULL,
                          `budget_amount` DECIMAL(19, 0) DEFAULT 0,

                          CONSTRAINT `fk_budget_company`
                              FOREIGN KEY (`company_id`) REFERENCES `company`(`id`),
                          CONSTRAINT `fk_budget_department`
                              FOREIGN KEY (`department_id`) REFERENCES `department`(`id`),
                          CONSTRAINT `fk_budget_gl_account`
                              FOREIGN KEY (`gl_account_id`) REFERENCES `gl_account`(`id`),

                          CONSTRAINT `uk_budget_dept_acc_month`
                              UNIQUE (`department_id`, `gl_account_id`, `year_month`)
);

CREATE TABLE `journal_entry` (
                                 `id` VARCHAR(36) PRIMARY KEY,
                                 `company_id` VARCHAR(36) NOT NULL,
                                 `entry_date` DATE NOT NULL,
                                 `description` VARCHAR(255),
                                 `party_id` VARCHAR(36),
                                 `bank_account_id` VARCHAR(36),
                                 `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
                                 `created_by` VARCHAR(50) DEFAULT 'SYSTEM',

                                 CONSTRAINT `fk_journal_company`
                                     FOREIGN KEY (`company_id`) REFERENCES `company`(`id`),
                                 CONSTRAINT `fk_journal_party`
                                     FOREIGN KEY (`party_id`) REFERENCES `party`(`id`),
                                 CONSTRAINT `fk_journal_bank`
                                     FOREIGN KEY (`bank_account_id`) REFERENCES `bank_account`(`id`)
);

CREATE TABLE `journal_line` (
                                `id` VARCHAR(36) PRIMARY KEY,
                                `journal_entry_id` VARCHAR(36) NOT NULL,
                                `gl_account_id` VARCHAR(36) NOT NULL,
                                `department_id` VARCHAR(36),
                                `debit_amount` DECIMAL(19, 0) DEFAULT 0,
                                `credit_amount` DECIMAL(19, 0) DEFAULT 0,
                                `memo` VARCHAR(255),

                                CONSTRAINT `fk_line_entry`
                                    FOREIGN KEY (`journal_entry_id`) REFERENCES `journal_entry`(`id`) ON DELETE CASCADE,
                                CONSTRAINT `fk_line_gl`
                                    FOREIGN KEY (`gl_account_id`) REFERENCES `gl_account`(`id`),
                                CONSTRAINT `fk_line_dept`
                                    FOREIGN KEY (`department_id`) REFERENCES `department`(`id`)
);

-- 3. Seed 데이터

INSERT INTO `company` (`id`, `name`, `business_no`) VALUES
    ('CP-001', 'Team SIU Corp', '123-45-67890');

INSERT INTO `department` (`id`, `company_id`, `name`, `code`) VALUES
                                                                  ('DEPT-01', 'CP-001', '개발팀', 'DEV'),
                                                                  ('DEPT-02', 'CP-001', '영업팀', 'SALES'),
                                                                  ('DEPT-03', 'CP-001', '경영지원팀', 'MGT');

INSERT INTO `gl_account` (`id`, `company_id`, `code`, `name`, `type`) VALUES
                                                                          ('ACC-001', 'CP-001', '101', '보통예금', 'ASSET'),
                                                                          ('ACC-002', 'CP-001', '401', '용역매출', 'REVENUE'),
                                                                          ('ACC-003', 'CP-001', '811', '복리후생비(식대)', 'EXPENSE'),
                                                                          ('ACC-004', 'CP-001', '812', '도서인쇄비', 'EXPENSE'),
                                                                          ('ACC-005', 'CP-001', '813', '서버유지비', 'EXPENSE');

INSERT INTO `party` (`id`, `company_id`, `name`, `type`, `contact`) VALUES
                                                                        ('PTY-001', 'CP-001', '삼성전자', 'CUSTOMER', '김삼성 010-1111-2222'),
                                                                        ('PTY-002', 'CP-001', 'AWS Korea', 'VENDOR', 'cloud@aws.com'),
                                                                        ('PTY-003', 'CP-001', '스타벅스', 'VENDOR', '매장결제');

INSERT INTO `bank_account` (`id`, `company_id`, `bank_name`, `account_no`, `account_alias`, `description`) VALUES
    ('BANK-001', 'CP-001', '국민은행', '111-222-333333', '메인운영통장', '법인카드 결제 계좌');

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) VALUES
                                                                                                               ('BUD-001', 'CP-001', 'DEPT-01', 'ACC-003', '202501', 1000000),
                                                                                                               ('BUD-002', 'CP-001', 'DEPT-01', 'ACC-005', '202501', 500000);
