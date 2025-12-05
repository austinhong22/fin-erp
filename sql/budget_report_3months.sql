-- ============================================
-- 3개월치 예산 대비 실적 리포트용 더미 데이터
-- ============================================
-- 기간: 2025년 2월 ~ 2025년 3월 (202501은 dummy_data.sql에 있음)
-- 사용법: 
--   1. init.sql 실행 (테이블 생성 및 기본 데이터)
--   2. dummy_data.sql 실행 (2025년 1월 데이터)
--   3. add_uuid_party_journals.sql 실행 (2025년 1월 추가 데이터)
--   4. 이 파일(budget_report_3months.sql) 실행 (2025년 2~3월 데이터)
--
-- 주의사항:
--   - 기존 데이터와 충돌할 수 있으므로 테스트 DB에서만 사용하세요.
--   - UUID() 함수는 MySQL 8.0 이상에서 사용 가능합니다.
--   - 이전 버전을 사용하는 경우, COALESCE(UUID(), '고정ID')로 대체됩니다.
--
-- 포함된 더미 데이터:
--   - 예산: 202502~202503 각 월별 부서/계정 조합
--   - 전표: 각 월별 매출/지출 전표 (차변=대변 균형 유지)

-- ============================================
-- 1. 2025년 2월 예산 추가
-- ============================================
-- 개발팀 예산
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202502-001')),
    'CP-001',
    d.id,
    a.id,
    '202502',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '811' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202502'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202502-002')),
    'CP-001',
    d.id,
    a.id,
    '202502',
    1500000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '813' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202502'
  )
LIMIT 1;

-- 영업팀 예산
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202502-003')),
    'CP-001',
    d.id,
    a.id,
    '202502',
    1000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '816' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202502'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202502-004')),
    'CP-001',
    d.id,
    a.id,
    '202502',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '817' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202502'
  )
LIMIT 1;

-- 경영지원팀 예산
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202502-005')),
    'CP-001',
    d.id,
    a.id,
    '202502',
    3000000
FROM `department` d, `gl_account` a
WHERE d.code = 'MGT' AND a.code = '814' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202502'
  )
LIMIT 1;

-- ============================================
-- 2. 2025년 2월 전표 추가
-- ============================================
-- 전표 1: LG전자 매출
SET @entry_id_feb_1 = COALESCE(UUID(), CONCAT('JE-202502-001'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_feb_1,
    'CP-001',
    '2025-02-05',
    'LG전자 프로젝트 계약금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = 'LG전자' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 1 라인: LG전자 매출 (차변: 보통예금 5,000,000 / 대변: 용역매출 5,000,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202502-001')),
    @entry_id_feb_1,
    a1.id,  -- 보통예금
    d.id,   -- 영업팀
    5000000,
    0,
    'LG전자 프로젝트 계약금 입금'
FROM `gl_account` a1, `department` d
WHERE a1.code = '101' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202502-002')),
    @entry_id_feb_1,
    a2.id,  -- 용역매출
    d.id,   -- 영업팀
    0,
    5000000,
    'LG전자 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- 전표 2: AWS 서버비용 지출
SET @entry_id_feb_2 = COALESCE(UUID(), CONCAT('JE-202502-002'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_feb_2,
    'CP-001',
    '2025-02-12',
    'AWS 클라우드 서버 유지비 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = 'AWS Korea' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 2 라인: AWS 서버비용 (차변: 서버유지비 1,200,000 / 대변: 보통예금 1,200,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202502-003')),
    @entry_id_feb_2,
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
    COALESCE(UUID(), CONCAT('JL-202502-004')),
    @entry_id_feb_2,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    1200000,
    'AWS 서버비용 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- 전표 3: 영업팀 여비교통비 지출
SET @entry_id_feb_3 = COALESCE(UUID(), CONCAT('JE-202502-003'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_feb_3,
    'CP-001',
    '2025-02-20',
    '영업팀 출장비 지급',
    CAST(NULL AS CHAR(36)),
    b.id
FROM `bank_account` b
WHERE b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 3 라인: 영업팀 여비교통비 (차변: 여비교통비 800,000 / 대변: 보통예금 800,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202502-005')),
    @entry_id_feb_3,
    a1.id,  -- 여비교통비
    d.id,   -- 영업팀
    800000,
    0,
    '영업팀 출장비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '816' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202502-006')),
    @entry_id_feb_3,
    a2.id,  -- 보통예금
    d.id,   -- 영업팀
    0,
    800000,
    '출장비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'SALES'
LIMIT 1;

-- ============================================
-- 3. 2025년 3월 예산 추가
-- ============================================
-- 개발팀 예산
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202503-001')),
    'CP-001',
    d.id,
    a.id,
    '202503',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '811' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202503'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202503-002')),
    'CP-001',
    d.id,
    a.id,
    '202503',
    1500000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '813' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202503'
  )
LIMIT 1;

-- 영업팀 예산
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202503-003')),
    'CP-001',
    d.id,
    a.id,
    '202503',
    1000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '816' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202503'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202503-004')),
    'CP-001',
    d.id,
    a.id,
    '202503',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '817' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202503'
  )
LIMIT 1;

-- 경영지원팀 예산
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202503-005')),
    'CP-001',
    d.id,
    a.id,
    '202503',
    3000000
FROM `department` d, `gl_account` a
WHERE d.code = 'MGT' AND a.code = '814' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202503'
  )
LIMIT 1;

-- ============================================
-- 4. 2025년 3월 전표 추가
-- ============================================
-- 전표 1: 네이버 매출
SET @entry_id_mar_1 = COALESCE(UUID(), CONCAT('JE-202503-001'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_mar_1,
    'CP-001',
    '2025-03-08',
    '네이버 시스템 구축 프로젝트 대금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '네이버' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 1 라인: 네이버 매출 (차변: 보통예금 8,000,000 / 대변: 용역매출 8,000,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202503-001')),
    @entry_id_mar_1,
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
    COALESCE(UUID(), CONCAT('JL-202503-002')),
    @entry_id_mar_1,
    a2.id,  -- 용역매출
    d.id,   -- 영업팀
    0,
    8000000,
    '네이버 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- 전표 2: 개발팀 식대 지출
SET @entry_id_mar_2 = COALESCE(UUID(), CONCAT('JE-202503-002'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_mar_2,
    'CP-001',
    '2025-03-15',
    '개발팀 회식비 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '스타벅스' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 2 라인: 개발팀 식대 (차변: 복리후생비 350,000 / 대변: 보통예금 350,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202503-003')),
    @entry_id_mar_2,
    a1.id,  -- 복리후생비
    d.id,   -- 개발팀
    350000,
    0,
    '개발팀 회식비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '811' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202503-004')),
    @entry_id_mar_2,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    350000,
    '회식비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- 전표 3: 영업팀 광고선전비 지출
SET @entry_id_mar_3 = COALESCE(UUID(), CONCAT('JE-202503-003'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_mar_3,
    'CP-001',
    '2025-03-25',
    '영업팀 광고비 지급',
    CAST(NULL AS CHAR(36)),
    b.id
FROM `bank_account` b
WHERE b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 3 라인: 영업팀 광고선전비 (차변: 광고선전비 1,500,000 / 대변: 보통예금 1,500,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202503-005')),
    @entry_id_mar_3,
    a1.id,  -- 광고선전비
    d.id,   -- 영업팀
    1500000,
    0,
    '영업팀 광고비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '817' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202503-006')),
    @entry_id_mar_3,
    a2.id,  -- 보통예금
    d.id,   -- 영업팀
    0,
    1500000,
    '광고비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'SALES'
LIMIT 1;

-- ============================================
-- 완료 메시지
-- ============================================
SELECT '3개월치 예산 대비 실적 리포트용 더미 데이터 삽입이 완료되었습니다!' AS message;
SELECT 
    year_month,
    COUNT(*) AS '예산 수'
FROM budget
WHERE year_month IN ('202502', '202503')
GROUP BY year_month
ORDER BY year_month;
SELECT 
    DATE_FORMAT(STR_TO_DATE(entry_date, '%Y-%m-%d'), '%Y%m') AS '연월',
    COUNT(*) AS '전표 헤더 수'
FROM journal_entry
WHERE entry_date >= '2025-02-01' AND entry_date < '2025-04-01'
GROUP BY DATE_FORMAT(STR_TO_DATE(entry_date, '%Y-%m-%d'), '%Y%m')
ORDER BY DATE_FORMAT(STR_TO_DATE(entry_date, '%Y-%m-%d'), '%Y%m');

