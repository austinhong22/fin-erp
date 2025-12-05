-- ============================================
-- 6개월치 예산 대비 실적 리포트용 더미 데이터
-- ============================================
-- 기간: 2025년 2월 ~ 2025년 6월 (202501은 dummy_data.sql에 있음)
-- 사용법: 
--   1. init.sql 실행 (테이블 생성 및 기본 데이터)
--   2. dummy_data.sql 실행 (2025년 1월 데이터)
--   3. 이 파일(budget_report_6months.sql) 실행 (2025년 2~6월 데이터)
--
-- 주의사항:
--   - 기존 데이터와 충돌할 수 있으므로 테스트 DB에서만 사용하세요.
--   - UUID() 함수는 MySQL 8.0 이상에서 사용 가능합니다.
--   - 이전 버전을 사용하는 경우, COALESCE(UUID(), '고정ID')로 대체됩니다.
--
-- 포함된 더미 데이터:
--   - 예산: 202502~202506 각 월별 부서/계정 조합
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
    'LG전자 2월 프로젝트 계약금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = 'LG전자' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 1 라인: LG전자 매출 (차변: 보통예금 4,000,000 / 대변: 용역매출 4,000,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202502-001')),
    @entry_id_feb_1,
    a1.id,  -- 보통예금
    d.id,   -- 영업팀
    4000000,
    0,
    'LG전자 2월 계약금 입금'
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
    4000000,
    'LG전자 2월 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- 전표 2: 개발팀 식대 지출
SET @entry_id_feb_2 = COALESCE(UUID(), CONCAT('JE-202502-002'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_feb_2,
    'CP-001',
    '2025-02-12',
    '개발팀 2월 회식비 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '스타벅스' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 2 라인: 개발팀 식대 (차변: 복리후생비 250,000 / 대변: 보통예금 250,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202502-003')),
    @entry_id_feb_2,
    a1.id,  -- 복리후생비
    d.id,   -- 개발팀
    250000,
    0,
    '개발팀 2월 회식비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '811' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202502-004')),
    @entry_id_feb_2,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    250000,
    '회식비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- 전표 3: 구글클라우드 서버비용 지출
SET @entry_id_feb_3 = COALESCE(UUID(), CONCAT('JE-202502-003'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_feb_3,
    'CP-001',
    '2025-02-18',
    '구글클라우드 2월 서버 사용료 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE (p.name = '구글클라우드' OR p.name LIKE '구글%') AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 3 라인: 구글클라우드 서버비용 (차변: 서버유지비 900,000 / 대변: 보통예금 900,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202502-005')),
    @entry_id_feb_3,
    a1.id,  -- 서버유지비
    d.id,   -- 개발팀
    900000,
    0,
    '구글클라우드 2월 서버 월 사용료'
FROM `gl_account` a1, `department` d
WHERE a1.code = '813' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202502-006')),
    @entry_id_feb_3,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    900000,
    '구글클라우드 서버비용 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- ============================================
-- 3. 2025년 3월 예산 추가
-- ============================================
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
    '네이버 3월 프로젝트 완료 대금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '네이버' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 1 라인: 네이버 매출 (차변: 보통예금 6,000,000 / 대변: 용역매출 6,000,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202503-001')),
    @entry_id_mar_1,
    a1.id,  -- 보통예금
    d.id,   -- 영업팀
    6000000,
    0,
    '네이버 3월 프로젝트 대금 입금'
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
    6000000,
    '네이버 3월 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- 전표 2: 임대료 지출
SET @entry_id_mar_2 = COALESCE(UUID(), CONCAT('JE-202503-002'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_mar_2,
    'CP-001',
    '2025-03-15',
    '3월 사무실 임대료 지급',
    CAST(NULL AS CHAR(36)),
    b.id
FROM `bank_account` b
WHERE b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 2 라인: 임대료 (차변: 임대료 2,500,000 / 대변: 보통예금 2,500,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202503-003')),
    @entry_id_mar_2,
    a1.id,  -- 임대료
    d.id,   -- 경영지원팀
    2500000,
    0,
    '3월 사무실 임대료 지급'
FROM `gl_account` a1, `department` d
WHERE a1.code = '814' AND d.code = 'MGT'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202503-004')),
    @entry_id_mar_2,
    a2.id,  -- 보통예금
    d.id,   -- 경영지원팀
    0,
    2500000,
    '임대료 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'MGT'
LIMIT 1;

-- 전표 3: 개발팀 식대 지출
SET @entry_id_mar_3 = COALESCE(UUID(), CONCAT('JE-202503-003'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_mar_3,
    'CP-001',
    '2025-03-22',
    '개발팀 3월 회식비 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '스타벅스' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 3 라인: 개발팀 식대 (차변: 복리후생비 350,000 / 대변: 보통예금 350,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202503-005')),
    @entry_id_mar_3,
    a1.id,  -- 복리후생비
    d.id,   -- 개발팀
    350000,
    0,
    '개발팀 3월 회식비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '811' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202503-006')),
    @entry_id_mar_3,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    350000,
    '회식비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- ============================================
-- 5. 2025년 4월 예산 추가
-- ============================================
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202504-001')),
    'CP-001',
    d.id,
    a.id,
    '202504',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '811' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202504'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202504-002')),
    'CP-001',
    d.id,
    a.id,
    '202504',
    1500000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '813' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202504'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202504-003')),
    'CP-001',
    d.id,
    a.id,
    '202504',
    1000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '816' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202504'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202504-004')),
    'CP-001',
    d.id,
    a.id,
    '202504',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '817' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202504'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202504-005')),
    'CP-001',
    d.id,
    a.id,
    '202504',
    3000000
FROM `department` d, `gl_account` a
WHERE d.code = 'MGT' AND a.code = '814' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202504'
  )
LIMIT 1;

-- ============================================
-- 6. 2025년 4월 전표 추가
-- ============================================
-- 전표 1: 카카오 매출
SET @entry_id_apr_1 = COALESCE(UUID(), CONCAT('JE-202504-001'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_apr_1,
    'CP-001',
    '2025-04-03',
    '카카오 4월 시스템 구축 프로젝트 대금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '카카오' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 1 라인: 카카오 매출 (차변: 보통예금 10,000,000 / 대변: 용역매출 10,000,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202504-001')),
    @entry_id_apr_1,
    a1.id,  -- 보통예금
    d.id,   -- 영업팀
    10000000,
    0,
    '카카오 4월 프로젝트 대금 입금'
FROM `gl_account` a1, `department` d
WHERE a1.code = '101' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202504-002')),
    @entry_id_apr_1,
    a2.id,  -- 용역매출
    d.id,   -- 영업팀
    0,
    10000000,
    '카카오 4월 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- 전표 2: 오라클 라이선스비 지출
SET @entry_id_apr_2 = COALESCE(UUID(), CONCAT('JE-202504-002'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_apr_2,
    'CP-001',
    '2025-04-12',
    '오라클 4월 DB 라이선스비 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '오라클' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 2 라인: 오라클 라이선스비 (차변: 서버유지비 1,200,000 / 대변: 보통예금 1,200,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202504-003')),
    @entry_id_apr_2,
    a1.id,  -- 서버유지비
    d.id,   -- 개발팀
    1200000,
    0,
    '오라클 DB 라이선스비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '813' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202504-004')),
    @entry_id_apr_2,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    1200000,
    '오라클 라이선스비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- 전표 3: 영업팀 여비교통비 지출
SET @entry_id_apr_3 = COALESCE(UUID(), CONCAT('JE-202504-003'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_apr_3,
    'CP-001',
    '2025-04-20',
    '영업팀 4월 출장비 지급',
    CAST(NULL AS CHAR(36)),
    b.id
FROM `bank_account` b
WHERE b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 3 라인: 영업팀 여비교통비 (차변: 여비교통비 800,000 / 대변: 보통예금 800,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202504-005')),
    @entry_id_apr_3,
    a1.id,  -- 여비교통비
    d.id,   -- 영업팀
    800000,
    0,
    '영업팀 4월 출장비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '816' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202504-006')),
    @entry_id_apr_3,
    a2.id,  -- 보통예금
    d.id,   -- 영업팀
    0,
    800000,
    '출장비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'SALES'
LIMIT 1;

-- ============================================
-- 7. 2025년 5월 예산 추가
-- ============================================
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202505-001')),
    'CP-001',
    d.id,
    a.id,
    '202505',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '811' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202505'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202505-002')),
    'CP-001',
    d.id,
    a.id,
    '202505',
    1500000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '813' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202505'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202505-003')),
    'CP-001',
    d.id,
    a.id,
    '202505',
    1000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '816' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202505'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202505-004')),
    'CP-001',
    d.id,
    a.id,
    '202505',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '817' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202505'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202505-005')),
    'CP-001',
    d.id,
    a.id,
    '202505',
    3000000
FROM `department` d, `gl_account` a
WHERE d.code = 'MGT' AND a.code = '814' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202505'
  )
LIMIT 1;

-- ============================================
-- 8. 2025년 5월 전표 추가
-- ============================================
-- 전표 1: 삼성전자 매출
SET @entry_id_may_1 = COALESCE(UUID(), CONCAT('JE-202505-001'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_may_1,
    'CP-001',
    '2025-05-07',
    '삼성전자 5월 용역매출 계약금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '삼성전자' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 1 라인: 삼성전자 매출 (차변: 보통예금 7,000,000 / 대변: 용역매출 7,000,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202505-001')),
    @entry_id_may_1,
    a1.id,  -- 보통예금
    d.id,   -- 영업팀
    7000000,
    0,
    '삼성전자 5월 계약금 입금'
FROM `gl_account` a1, `department` d
WHERE a1.code = '101' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202505-002')),
    @entry_id_may_1,
    a2.id,  -- 용역매출
    d.id,   -- 영업팀
    0,
    7000000,
    '삼성전자 5월 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- 전표 2: 마이크로소프트 클라우드비 지출
SET @entry_id_may_2 = COALESCE(UUID(), CONCAT('JE-202505-002'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_may_2,
    'CP-001',
    '2025-05-15',
    '마이크로소프트 Azure 5월 클라우드 사용료 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '마이크로소프트' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 2 라인: 마이크로소프트 클라우드비 (차변: 서버유지비 600,000 / 대변: 보통예금 600,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202505-003')),
    @entry_id_may_2,
    a1.id,  -- 서버유지비
    d.id,   -- 개발팀
    600000,
    0,
    'Azure 5월 클라우드 월 사용료'
FROM `gl_account` a1, `department` d
WHERE a1.code = '813' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202505-004')),
    @entry_id_may_2,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    600000,
    'Azure 클라우드비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- 전표 3: 영업팀 광고선전비 지출
SET @entry_id_may_3 = COALESCE(UUID(), CONCAT('JE-202505-003'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_may_3,
    'CP-001',
    '2025-05-25',
    '영업팀 5월 광고비 지급',
    CAST(NULL AS CHAR(36)),
    b.id
FROM `bank_account` b
WHERE b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 3 라인: 영업팀 광고선전비 (차변: 광고선전비 1,500,000 / 대변: 보통예금 1,500,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202505-005')),
    @entry_id_may_3,
    a1.id,  -- 광고선전비
    d.id,   -- 영업팀
    1500000,
    0,
    '영업팀 5월 광고비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '817' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202505-006')),
    @entry_id_may_3,
    a2.id,  -- 보통예금
    d.id,   -- 영업팀
    0,
    1500000,
    '광고비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'SALES'
LIMIT 1;

-- ============================================
-- 9. 2025년 6월 예산 추가
-- ============================================
INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202506-001')),
    'CP-001',
    d.id,
    a.id,
    '202506',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '811' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202506'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202506-002')),
    'CP-001',
    d.id,
    a.id,
    '202506',
    1500000
FROM `department` d, `gl_account` a
WHERE d.code = 'DEV' AND a.code = '813' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202506'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202506-003')),
    'CP-001',
    d.id,
    a.id,
    '202506',
    1000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '816' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202506'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202506-004')),
    'CP-001',
    d.id,
    a.id,
    '202506',
    2000000
FROM `department` d, `gl_account` a
WHERE d.code = 'SALES' AND a.code = '817' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202506'
  )
LIMIT 1;

INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) 
SELECT 
    COALESCE(UUID(), CONCAT('BUD-202506-005')),
    'CP-001',
    d.id,
    a.id,
    '202506',
    3000000
FROM `department` d, `gl_account` a
WHERE d.code = 'MGT' AND a.code = '814' 
  AND d.is_active = 'Y' AND a.is_active = 'Y'
  AND NOT EXISTS (
      SELECT 1 FROM `budget` b 
      WHERE b.department_id = d.id 
        AND b.gl_account_id = a.id 
        AND b.year_month = '202506'
  )
LIMIT 1;

-- ============================================
-- 10. 2025년 6월 전표 추가
-- ============================================
-- 전표 1: LG전자 매출
SET @entry_id_jun_1 = COALESCE(UUID(), CONCAT('JE-202506-001'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_jun_1,
    'CP-001',
    '2025-06-05',
    'LG전자 6월 프로젝트 완료 대금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = 'LG전자' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 1 라인: LG전자 매출 (차변: 보통예금 5,500,000 / 대변: 용역매출 5,500,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202506-001')),
    @entry_id_jun_1,
    a1.id,  -- 보통예금
    d.id,   -- 영업팀
    5500000,
    0,
    'LG전자 6월 프로젝트 대금 입금'
FROM `gl_account` a1, `department` d
WHERE a1.code = '101' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202506-002')),
    @entry_id_jun_1,
    a2.id,  -- 용역매출
    d.id,   -- 영업팀
    0,
    5500000,
    'LG전자 6월 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- 전표 2: AWS 서버비용 지출
SET @entry_id_jun_2 = COALESCE(UUID(), CONCAT('JE-202506-002'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_jun_2,
    'CP-001',
    '2025-06-10',
    'AWS 클라우드 6월 서버 유지비 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = 'AWS Korea' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 2 라인: AWS 서버비용 (차변: 서버유지비 1,200,000 / 대변: 보통예금 1,200,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202506-003')),
    @entry_id_jun_2,
    a1.id,  -- 서버유지비
    d.id,   -- 개발팀
    1200000,
    0,
    'AWS 클라우드 6월 서버 월 사용료'
FROM `gl_account` a1, `department` d
WHERE a1.code = '813' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202506-004')),
    @entry_id_jun_2,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    1200000,
    'AWS 서버비용 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- 전표 3: 개발팀 식대 지출
SET @entry_id_jun_3 = COALESCE(UUID(), CONCAT('JE-202506-003'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_jun_3,
    'CP-001',
    '2025-06-18',
    '개발팀 6월 회식비 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '스타벅스' AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 3 라인: 개발팀 식대 (차변: 복리후생비 400,000 / 대변: 보통예금 400,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202506-005')),
    @entry_id_jun_3,
    a1.id,  -- 복리후생비
    d.id,   -- 개발팀
    400000,
    0,
    '개발팀 6월 회식비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '811' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-202506-006')),
    @entry_id_jun_3,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    400000,
    '회식비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- ============================================
-- 완료 메시지
-- ============================================
SELECT '6개월치 예산 대비 실적 리포트용 더미 데이터 삽입이 완료되었습니다!' AS message;
SELECT 
    year_month,
    COUNT(*) AS '예산 수'
FROM budget
WHERE year_month IN ('202502', '202503', '202504', '202505', '202506')
GROUP BY year_month
ORDER BY year_month;
SELECT 
    DATE_FORMAT(STR_TO_DATE(entry_date, '%Y-%m-%d'), '%Y%m') AS '연월',
    COUNT(*) AS '전표 헤더 수'
FROM journal_entry
WHERE entry_date >= '2025-02-01' AND entry_date < '2025-07-01'
GROUP BY DATE_FORMAT(STR_TO_DATE(entry_date, '%Y-%m-%d'), '%Y%m')
ORDER BY DATE_FORMAT(STR_TO_DATE(entry_date, '%Y-%m-%d'), '%Y%m');

