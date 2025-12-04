package org.example.service;

import org.example.config.AppConfig;
import org.example.dao.BudgetDAO;
import org.example.dao.DepartmentDAO;
import org.example.dao.AccountDAO;
import org.example.dto.BudgetDTO;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class BudgetService {

    private final BudgetDAO budgetDAO = new BudgetDAO();
    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final AccountDAO glAccountDAO = new AccountDAO();

    // 유효성 검증 헬퍼 메서드 (★ DB 오류는 내부적으로 처리)
    private void validateYearMonth(String ym) {
        if (ym == null || !ym.matches("\\d{6}")) throw new IllegalArgumentException("연월은 yyyyMM 형식의 숫자 6자리여야 합니다.");
        int year = Integer.parseInt(ym.substring(0, 4));
        int month = Integer.parseInt(ym.substring(4, 6));
        if (year < 2000 || year > 2100) throw new IllegalArgumentException("연도는 2000~2100 사이여야 합니다.");
        if (month < 1 || month > 12) throw new IllegalArgumentException("월은 01~12 사이여야 합니다.");
    }

    private void validateDept(String deptId) {
        try {
            if (departmentDAO.selectById(deptId) == null)
                throw new IllegalArgumentException("존재하지 않는 부서입니다: " + deptId);
        } catch (SQLException e) {
            throw new IllegalStateException("부서 조회 중 DB 오류 발생: " + e.getMessage(), e);
        }
    }

    private void validateGlAccount(String accountId) {
        try {
            if (glAccountDAO.selectById(accountId) == null) {
                throw new IllegalArgumentException("존재하지 않는 계정과목입니다: " + accountId);
            }
        } catch (SQLException e) {
            throw new IllegalStateException("계정 조회 중 DB 오류 발생: " + e.getMessage(), e);
        }
    }

    // 1. 예산 등록 (Create)
    public BudgetDTO registerBudget(String deptId,
                                    String glAccountId,
                                    String yearMonth,
                                    BigDecimal amount) {

        deptId = deptId.toUpperCase();
        glAccountId = glAccountId.toUpperCase();

        try { // ★ Public 메서드 전체를 try-catch로 감쌉니다.
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
                    UUID.randomUUID().toString(), AppConfig.COMPANY_ID, deptId, glAccountId, yearMonth, amount
            );

            budgetDAO.insert(dto);
            return dto;
        } catch (SQLException e) {
            // DB 오류 발생 시 Service가 최종 처리합니다.
            throw new IllegalStateException("예산 등록 중 DB 오류 발생: " + e.getMessage(), e);
        }
    }

    // 2. 예산 목록 조회 (Read)
    public List<BudgetDTO> getBudgets(String companyId) {
        try {
            return budgetDAO.selectByCompanyId(companyId);
        } catch (SQLException e) {
            throw new IllegalStateException("예산 목록 조회 중 DB 오류 발생: " + e.getMessage(), e);
        }
    }

    // 3. 예산 금액 수정 (Update)
    public void updateBudgetAmount(String budgetId, BigDecimal newAmount) {
        if (newAmount == null || newAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("수정할 금액은 0보다 커야 합니다.");
        }

        try {
            int result = budgetDAO.updateAmount(budgetId, newAmount);
            if (result == 0) {
                throw new IllegalArgumentException("예산 수정 실패: 해당 ID를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("예산 수정 중 DB 오류 발생: " + e.getMessage(), e);
        }
    }

    // 4. 예산 삭제 (Delete)
    public void deleteBudget(String budgetId) {
        try {
            int result = budgetDAO.delete(budgetId);
            if (result == 0) {
                throw new IllegalArgumentException("예산 삭제 실패: 해당 ID를 찾을 수 없습니다.");
            }
        } catch (SQLException e) {
            throw new IllegalStateException("예산 삭제 중 DB 오류 발생: " + e.getMessage(), e);
        }
    }
}