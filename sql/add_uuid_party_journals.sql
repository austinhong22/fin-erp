-- ============================================
-- UUID 거래처에 전표 추가 (원장 조회 테스트용)
-- ============================================
-- 사용법: 이 파일을 실행하면 UUID로 된 거래처들에도 전표가 추가됩니다.
-- 원장 조회 테스트를 위해 UUID 거래처에 거래 내역을 추가합니다.

-- ============================================
-- 전표 7: LG전자 (UUID) 매출
-- ============================================
SET @entry_id_7 = COALESCE(UUID(), CONCAT('JE-', DATE_FORMAT(NOW(), '%Y%m%d'), '-007'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_7,
    'CP-001',
    '2025-01-12',
    'LG전자 프로젝트 계약금 입금',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = 'LG전자' AND p.id LIKE '%-%-%-%-%'  -- UUID 형식만 선택
  AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 7 라인: LG전자 매출 (차변: 보통예금 3,000,000 / 대변: 용역매출 3,000,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-013')),
    @entry_id_7,
    a1.id,  -- 보통예금
    d.id,   -- 영업팀
    3000000,
    0,
    'LG전자 계약금 입금'
FROM `gl_account` a1, `department` d
WHERE a1.code = '101' AND d.code = 'SALES'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-014')),
    @entry_id_7,
    a2.id,  -- 용역매출
    d.id,   -- 영업팀
    0,
    3000000,
    'LG전자 프로젝트 매출'
FROM `gl_account` a2, `department` d
WHERE a2.code = '401' AND d.code = 'SALES'
LIMIT 1;

-- ============================================
-- 전표 8: 구글클라우드 (UUID) 서버비용 지출
-- ============================================
SET @entry_id_8 = COALESCE(UUID(), CONCAT('JE-', DATE_FORMAT(NOW(), '%Y%m%d'), '-008'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_8,
    'CP-001',
    '2025-01-18',
    '구글클라우드 서버 사용료 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '구글클라우드' AND p.id LIKE '%-%-%-%-%'  -- UUID 형식만 선택
  AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 8 라인: 구글클라우드 서버비용 (차변: 서버유지비 100,000 / 대변: 보통예금 100,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-015')),
    @entry_id_8,
    a1.id,  -- 서버유지비
    d.id,   -- 개발팀
    100000,
    0,
    '구글클라우드 서버 월 사용료'
FROM `gl_account` a1, `department` d
WHERE a1.code = '813' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-016')),
    @entry_id_8,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    100000,
    '구글클라우드 서버비용 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- ============================================
-- 전표 9: 오라클 (UUID) 라이선스비 지출
-- ============================================
SET @entry_id_9 = COALESCE(UUID(), CONCAT('JE-', DATE_FORMAT(NOW(), '%Y%m%d'), '-009'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_9,
    'CP-001',
    '2025-01-22',
    '오라클 DB 라이선스비 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '오라클' AND p.id LIKE '%-%-%-%-%'  -- UUID 형식만 선택
  AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 9 라인: 오라클 라이선스비 (차변: 서버유지비 100,000 / 대변: 보통예금 100,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-017')),
    @entry_id_9,
    a1.id,  -- 서버유지비
    d.id,   -- 개발팀
    100000,
    0,
    '오라클 DB 라이선스비'
FROM `gl_account` a1, `department` d
WHERE a1.code = '813' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-018')),
    @entry_id_9,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    100000,
    '오라클 라이선스비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- ============================================
-- 전표 10: 마이크로소프트 (UUID) 클라우드비 지출
-- ============================================
SET @entry_id_10 = COALESCE(UUID(), CONCAT('JE-', DATE_FORMAT(NOW(), '%Y%m%d'), '-010'));
INSERT INTO `journal_entry` (`id`, `company_id`, `entry_date`, `description`, `party_id`, `bank_account_id`) 
SELECT 
    @entry_id_10,
    'CP-001',
    '2025-01-26',
    '마이크로소프트 Azure 클라우드 사용료 지급',
    p.id,
    b.id
FROM `party` p, `bank_account` b
WHERE p.name = '마이크로소프트' AND p.id LIKE '%-%-%-%-%'  -- UUID 형식만 선택
  AND b.account_alias = '메인운영통장'
LIMIT 1;

-- 전표 10 라인: 마이크로소프트 클라우드비 (차변: 서버유지비 100,000 / 대변: 보통예금 100,000)
INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-019')),
    @entry_id_10,
    a1.id,  -- 서버유지비
    d.id,   -- 개발팀
    100000,
    0,
    'Azure 클라우드 월 사용료'
FROM `gl_account` a1, `department` d
WHERE a1.code = '813' AND d.code = 'DEV'
LIMIT 1;

INSERT INTO `journal_line` (`id`, `journal_entry_id`, `gl_account_id`, `department_id`, `debit_amount`, `credit_amount`, `memo`)
SELECT 
    COALESCE(UUID(), CONCAT('JL-', DATE_FORMAT(NOW(), '%Y%m%d'), '-020')),
    @entry_id_10,
    a2.id,  -- 보통예금
    d.id,   -- 개발팀
    0,
    100000,
    'Azure 클라우드비 지급'
FROM `gl_account` a2, `department` d
WHERE a2.code = '101' AND d.code = 'DEV'
LIMIT 1;

-- ============================================
-- 완료 메시지
-- ============================================
SELECT 'UUID 거래처 전표 추가가 완료되었습니다!' AS message;
SELECT 
    p.id,
    p.name,
    COUNT(je.id) AS '전표 수'
FROM party p
LEFT JOIN journal_entry je ON p.id = je.party_id
WHERE p.id LIKE '%-%-%-%-%' AND p.is_active = 'Y'
GROUP BY p.id, p.name
ORDER BY p.name;





