-- ============================================
-- 테스트용 더미 데이터 삽입 SQL
-- ============================================
-- 사용법: 
--   1. init.sql 실행 (테이블 생성 및 기본 데이터)
--   2. 이 파일(dummy_data.sql) 실행 (더미 데이터 추가)
--
-- 주의사항:
--   - 기존 데이터와 충돌할 수 있으므로 테스트 DB에서만 사용하세요.
--   - UUID() 함수는 MySQL 8.0 이상에서 사용 가능합니다.
--   - 이전 버전을 사용하는 경우, COALESCE(UUID(), '고정ID')로 대체됩니다.
--
-- 포함된 더미 데이터:
--   - 부서: 5개 (인사팀, 재무팀, 마케팅팀, 고객지원팀, 연구개발팀)
--   - 계정과목: 9개 (자산, 수익, 비용 계정)
--   - 거래처: 8개 (고객 3개, 공급사 5개)
--   - 은행계좌: 3개
--   - 예산: 5개 (2025년 1월)
--   - 전표: 6개 (매출 3건, 지출 3건, 차변=대변 균형 유지)

-- ============================================
-- 1. 부서 추가 (is_active 포함)
-- ============================================
-- 주의: UUID() 함수는 MySQL 8.0 이상에서 사용 가능합니다.
-- 이전 버전을 사용하는 경우, 아래와 같이 고정 ID를 사용하세요.
-- 예: ('DEPT-04', 'CP-001', '인사팀', 'HR', 'Y')
INSERT INTO `department` (`id`, `company_id`, `name`, `code`, `is_active`) VALUES
    (COALESCE(UUID(), 'DEPT-04'), 'CP-001', '인사팀', 'HR', 'Y'),
    (COALESCE(UUID(), 'DEPT-05'), 'CP-001', '재무팀', 'FIN', 'Y'),
    (COALESCE(UUID(), 'DEPT-06'), 'CP-001', '마케팅팀', 'MKT', 'Y'),
    (COALESCE(UUID(), 'DEPT-07'), 'CP-001', '고객지원팀', 'CS', 'Y'),
    (COALESCE(UUID(), 'DEPT-08'), 'CP-001', '연구개발팀', 'RND', 'Y');

-- ============================================
-- 2. 계정과목 추가 (is_active 포함)
-- ============================================
INSERT INTO `gl_account` (`id`, `company_id`, `code`, `name`, `type`, `is_active`) VALUES
    (COALESCE(UUID(), 'ACC-006'), 'CP-001', '102', '당좌예금', 'ASSET', 'Y'),
    (COALESCE(UUID(), 'ACC-007'), 'CP-001', '201', '매입채무', 'ASSET', 'Y'),
    (COALESCE(UUID(), 'ACC-008'), 'CP-001', '301', '자본금', 'ASSET', 'Y'),
    (COALESCE(UUID(), 'ACC-009'), 'CP-001', '501', '제품매출', 'REVENUE', 'Y'),
    (COALESCE(UUID(), 'ACC-010'), 'CP-001', '601', '매출원가', 'EXPENSE', 'Y'),
    (COALESCE(UUID(), 'ACC-011'), 'CP-001', '814', '임대료', 'EXPENSE', 'Y'),
    (COALESCE(UUID(), 'ACC-012'), 'CP-001', '815', '통신비', 'EXPENSE', 'Y'),
    (COALESCE(UUID(), 'ACC-013'), 'CP-001', '816', '여비교통비', 'EXPENSE', 'Y'),
    (COALESCE(UUID(), 'ACC-014'), 'CP-001', '817', '광고선전비', 'EXPENSE', 'Y');

-- ============================================
-- 3. 거래처 추가 (is_active 포함)
-- ============================================
INSERT INTO `party` (`id`, `company_id`, `name`, `type`, `contact`, `registration_number`, `is_active`) VALUES
    (COALESCE(UUID(), 'PTY-004'), 'CP-001', 'LG전자', 'CUSTOMER', '이엘지 010-3333-4444', '123-45-11111', 'Y'),
    (COALESCE(UUID(), 'PTY-005'), 'CP-001', '네이버', 'CUSTOMER', '김네이버 010-5555-6666', '123-45-22222', 'Y'),
    (COALESCE(UUID(), 'PTY-006'), 'CP-001', '카카오', 'CUSTOMER', '박카카오 010-7777-8888', '123-45-33333', 'Y'),
    (COALESCE(UUID(), 'PTY-007'), 'CP-001', '구글클라우드', 'VENDOR', 'support@googlecloud.com', '123-45-44444', 'Y'),
    (COALESCE(UUID(), 'PTY-008'), 'CP-001', '오라클', 'VENDOR', 'sales@oracle.com', '123-45-55555', 'Y'),
    (COALESCE(UUID(), 'PTY-009'), 'CP-001', '마이크로소프트', 'VENDOR', 'azure@microsoft.com', '123-45-66666', 'Y'),
    (COALESCE(UUID(), 'PTY-010'), 'CP-001', '아마존웹서비스', 'VENDOR', 'aws-korea@amazon.com', '123-45-77777', 'Y'),
    (COALESCE(UUID(), 'PTY-011'), 'CP-001', '오피스월드', 'VENDOR', '02-1234-5678', '123-45-88888', 'Y');

-- ============================================
-- 4. 은행계좌 추가
-- ============================================
INSERT INTO `bank_account` (`id`, `company_id`, `bank_name`, `account_no`, `account_alias`, `description`) VALUES
    (COALESCE(UUID(), 'BANK-002'), 'CP-001', '신한은행', '110-444-555555', '급여통장', '직원 급여 지급용'),
    (COALESCE(UUID(), 'BANK-003'), 'CP-001', '우리은행', '1002-666-777777', '세금통장', '세금 납부 전용'),
    (COALESCE(UUID(), 'BANK-004'), 'CP-001', '하나은행', '123-888-999999', '예비금통장', '비상자금 보관');

-- ============================================
-- 5. 예산 추가 (2025년 1월)
-- ============================================
-- 주의: department_id와 gl_account_id는 서브쿼리로 조회합니다.
-- 기존 예산과 중복되지 않도록 주의하세요.

-- 개발팀 예산 (중복 방지: NOT EXISTS 사용)
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-', DATE_FORMAT(NOW(), '%Y%m%d'), '-001')),
    'CP-001',
    d.id,
    a.id,
    '202501',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '811' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202501'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-', DATE_FORMAT(NOW(), '%Y%m%d'), '-002')),
    'CP-001',
    d.id,
    a.id,
    '202501',
    1500000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '813' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202501'
  )
LIMIT 1;

-- 영업팀 예산 (중복 방지)
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-', DATE_FORMAT(NOW(), '%Y%m%d'), '-003')),
    'CP-001',
    d.id,
    a.id,
    '202501',
    1000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '816' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202501'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-', DATE_FORMAT(NOW(), '%Y%m%d'), '-004')),
    'CP-001',
    d.id,
    a.id,
    '202501',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '817' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202501'
  )
LIMIT 1;

-- 경영지원팀 예산 (중복 방지)
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-', DATE_FORMAT(NOW(), '%Y%m%d'), '-005')),
    'CP-001',
    d.id,
    a.id,
    '202501',
    3000000
FROM `department` d, `gl_account` a
WHERE d.code = 'MGT' AND a.code = '814' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202501'
  )
LIMIT 1;

-- ============================================
-- 6. 전표 헤더 (journal_entry) 추가
-- ============================================
-- 전표 1: 삼성전자 매출
SET @entry_id_1 = COALESCE(UUID(), CONCAT('JE-', DATE_FORMAT(NOW(), '%Y%m%d'), '-001'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_1,
    'CP-001',
    '2025-01-05',
    '삼성전자 용역매출 계약금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '삼성전자' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 2: AWS 서버비용 지출
SET @entry_id_2 = COALESCE(UUID(), CONCAT('JE-', DATE_FORMAT(NOW(), '%Y%m%d'), '-002'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_2,
    'CP-001',
    '2025-01-10',
    'AWS 클라우드 서버 유지비 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = 'AWS Korea' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 3: 개발팀 식대 지출
SET @entry_id_3 = COALESCE(UUID(), CONCAT('JE-', DATE_FORMAT(NOW(), '%Y%m%d'), '-003'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_3,
    'CP-001',
    '2025-01-15',
    '개발팀 회식비 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '스타벅스' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 4: 네이버 매출
SET @entry_id_4 = COALESCE(UUID(), CONCAT('JE-', DATE_FORMAT(NOW(), '%Y%m%d'), '-004'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_4,
    'CP-001',
    '2025-01-20',
    '네이버 프로젝트 완료 대금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '네이버' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 5: 임대료 지출
SET @entry_id_5 = COALESCE(UUID(), CONCAT('JE-', DATE_FORMAT(NOW(), '%Y%m%d'), '-005'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_5,
    'CP-001',
    '2025-01-25',
    '사무실 임대료 지급',
    CAST(NULL AS CHAR(36)),  -- 거래처 없음 (NULL을 명시적으로 캐스팅)
    b.id
FROM `bank_account` b
WHERE b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 6: 카카오 매출
SET @entry_id_6 = COALESCE(UUID(), CONCAT('JE-', DATE_FORMAT(NOW(), '%Y%m%d'), '-006'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_6,
    'CP-001',
    '2025-01-28',
    '카카오 시스템 구축 프로젝트 대금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '카카오' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- ============================================
-- 7. 전표 라인 (journal_line) 추가
-- ============================================
-- 중요: 차변 합계 = 대변 합계가 되어야 합니다 (복식부기 원칙)

-- 전표 1 라인: 삼성전자 매출 (차변: 보통예금 5,000,000 / 대변: 용역매출 5,000,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-001')),
    @entry_id_1,
    a1.id,  -- 보통예금
    d.id,   -- 영업팀
    5000000,
    0,
    '삼성전자 계약금 입금'
FROM `gl_account` a1, `department` d
WHERE a1.code = '101' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-002')),
    @entry_id_1,
    a2.id,  -- 용역매출
    d.id,   -- 영업팀
    0,
    5000000,
    '삼성전자 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- 전표 2 라인: AWS 서버비용 (차변: 서버유지비 1,200,000 / 대변: 보통예금 1,200,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-003')),
    @entry_id_2,
    a1.id,  -- 서버유지비
    d.id,   -- 개발팀
    1200000,
    0,
    'AWS 클라우드 서버 월 사용료'
FROM `gl_account` a1, `department` d
WHERE a1.code = '813' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-004')),
    @entry_id_2,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    1200000,
    'AWS 서버비용 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- 전표 3 라인: 개발팀 식대 (차변: 복리후생비 300,000 / 대변: 보통예금 300,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-005')),
    @entry_id_3,
    a1.id,  -- 복리후생비
    d.id,   -- 개발팀
    300000,
    0,
    '개발팀 회식비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '811' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-006')),
    @entry_id_3,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    300000,
    '회식비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- 전표 4 라인: 네이버 매출 (차변: 보통예금 8,000,000 / 대변: 용역매출 8,000,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-007')),
    @entry_id_4,
    a1.id,  -- 보통예금
    d.id,   -- 영업팀
    8000000,
    0,
    '네이버 프로젝트 대금 입금'
FROM `gl_account` a1, `department` d
WHERE a1.code = '101' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-008')),
    @entry_id_4,
    a2.id,  -- 용역매출
    d.id,   -- 영업팀
    0,
    8000000,
    '네이버 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- 전표 5 라인: 임대료 (차변: 임대료 2,500,000 / 대변: 보통예금 2,500,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-009')),
    @entry_id_5,
    a1.id,  -- 임대료
    d.id,   -- 경영지원팀
    2500000,
    0,
    '사무실 임대료 지급'
FROM `gl_account` a1, `department` d
WHERE a1.code = '814' AND d.code = 'MGT'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-010')),
    @entry_id_5,
    a2.id,  -- 보통예금
    d.id,   -- 경영지원팀
    0,
    2500000,
    '임대료 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'MGT'
LIMIT 1;

-- 전표 6 라인: 카카오 매출 (차변: 보통예금 12,000,000 / 대변: 용역매출 12,000,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-011')),
    @entry_id_6,
    a1.id,  -- 보통예금
    d.id,   -- 영업팀
    12000000,
    0,
    '카카오 프로젝트 대금 입금'
FROM `gl_account` a1, `department` d
WHERE a1.code = '101' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-012')),
    @entry_id_6,
    a2.id,  -- 용역매출
    d.id,   -- 영업팀
    0,
    12000000,
    '카카오 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- ============================================
-- 완료 메시지
-- ============================================
SELECT '더미 데이터 삽입이 완료되었습니다!' AS message;
SELECT COUNT(*) AS '부서 수' FROM department WHERE is_active = 'Y';
SELECT COUNT(*) AS '계정과목 수' FROM gl_account WHERE is_active = 'Y';
SELECT COUNT(*) AS '거래처 수' FROM party WHERE is_active = 'Y';
SELECT COUNT(*) AS '은행계좌 수' FROM bank_account;
SELECT COUNT(*) AS '예산 수' FROM budget;
SELECT COUNT(*) AS '전표 헤더 수' FROM journal_entry;
SELECT COUNT(*) AS '전표 라인 수' FROM journal_line;

