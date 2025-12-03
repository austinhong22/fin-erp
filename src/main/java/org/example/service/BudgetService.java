package org.example.service;

import org.example.config.AppConfig;
import org.example.dao.BudgetDAO;
import org.example.dao.DepartmentDAO;
import org.example.dao.AccountDAO;
import org.example.dto.BudgetDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class BudgetService {

    private final BudgetDAO budgetDAO = new BudgetDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final AccountDAO glAccountDAO = new AccountDAO();

    // ===========================================
    // 연월(yearMonth) 검증 메서드
    // ===========================================
    private void validateYearMonth(String ym) {
        if (ym == null || !ym.matches("\\d{6}"))
            throw new IllegalArgumentException("연월은 yyyyMM 형식의 숫자 6자리여야 합니다.");

        // 연도와 월 추출 및 범위 검증
        int year = Integer.parseInt(ym.substring(0, 4));
        int month = Integer.parseInt(ym.substring(4, 6));

        if (year < 2000 || year > 2100)
            throw new IllegalArgumentException("연도는 2000~2100 사이여야 합니다.");

        if (month < 1 || month > 12)
            throw new IllegalArgumentException("월은 01~12 사이여야 합니다.");
    }

    // ===========================================
    // 부서 ID 존재 여부 검증
    // ===========================================
    private void validateDept(String deptId) {
        if (departmentDAO.selectById(deptId) == null)
            throw new IllegalArgumentException("존재하지 않는 부서입니다: " + deptId);
    }

    // ===========================================
    // 계정 ID 존재 여부 검증
    // ===========================================
    private void validateGlAccount(String accountId) {
        // (주의: AccountDAO에 selectById가 있어야 작동합니다.)
        if (glAccountDAO.selectById(accountId) == null) {
            throw new IllegalArgumentException("존재하지 않는 계정과목입니다: " + accountId);
        }
    }

    // ===========================================
    // 1. 예산 등록 (Create)
    // ===========================================
    public BudgetDTO registerBudget(String deptId,
                                    String glAccountId,
                                    String yearMonth,
                                    BigDecimal amount) {

        deptId = deptId.toUpperCase();

        // 1. 유효성 검증
        validateDept(deptId);
        validateGlAccount(glAccountId);
        validateYearMonth(yearMonth);

        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("예산 금액은 0보다 커야 합니다.");

        // 2. 중복 체크
        if (budgetDAO.exists(deptId, glAccountId, yearMonth))
            throw new IllegalArgumentException("이미 등록된 예산입니다");

        // 3. DTO 생성 및 INSERT
        BudgetDTO dto = new BudgetDTO(
                UUID.randomUUID().toString(),
                AppConfig.COMPANY_ID,
                deptId,
                glAccountId,
                yearMonth,
                amount
        );

        budgetDAO.insert(dto);
        return dto;
    }

    // ===========================================
    // 2. 예산 목록 조회 (Read)
    // ===========================================
    public List<BudgetDTO> getBudgets(String companyId) {
        return budgetDAO.selectByCompanyId(companyId);
    }

    // ===========================================
    // 3. 예산 금액 수정 (Update) - ★ 추가됨
    // ===========================================
    public void updateBudgetAmount(String budgetId, BigDecimal newAmount) {
        // 금액 유효성 검사
        if (newAmount == null || newAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("수정할 금액은 0보다 커야 합니다.");
        }

        // DAO 호출
        int result = budgetDAO.updateAmount(budgetId, newAmount);

        // 결과 확인 (0이면 해당 ID가 없다는 뜻)
        if (result == 0) {
            throw new IllegalArgumentException("예산 수정 실패: 해당 ID(" + budgetId + ")를 찾을 수 없습니다.");
        }
    }

    // ===========================================
    // 4. 예산 삭제 (Delete) - ★ 추가됨
    // ===========================================
    public void deleteBudget(String budgetId) {
        // DAO 호출
        int result = budgetDAO.delete(budgetId);

        // 결과 확인
        if (result == 0) {
            throw new IllegalArgumentException("예산 삭제 실패: 해당 ID(" + budgetId + ")를 찾을 수 없습니다.");
        }
    }
}