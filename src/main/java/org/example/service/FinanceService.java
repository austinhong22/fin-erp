package org.example.service;

import org.example.dao.AccountDAO;
import org.example.dao.BankAccountDAO;
import org.example.dao.ReportDAO;
import org.example.dto.AccountDTO;
import org.example.dto.AccountType;
import org.example.dto.BankAccountDTO;
import org.example.dto.ReportDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class FinanceService {
    private final AccountDAO accountDAO = new AccountDAO();
    private final BankAccountDAO bankAccountDAO = new BankAccountDAO();
    private final ReportDAO reportDAO = new ReportDAO();
    private static final String DEFAULT_COMPANY_ID = "CP-001"; // 회사 ID 고정값

    // 1. 계정 등록 (Create)
    public void registerAccount(String code, String name, AccountType type) throws SQLException {
        AccountDTO dto = new AccountDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setCompanyId(DEFAULT_COMPANY_ID);
        dto.setCode(code);
        dto.setName(name);
        dto.setType(type);

        accountDAO.insert(dto);
        System.out.println("계정 등록 완료: " + dto);
    }

    // 2. 은행 계좌 등록 (Create)
    public void registerBankAccount(String bankName, String accountNo, String accountAlias, String description) throws SQLException {
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setCompanyId(DEFAULT_COMPANY_ID);
        dto.setBankName(bankName);
        dto.setAccountNo(accountNo);
        dto.setAccountAlias(accountAlias);
        dto.setDescription(description);

        bankAccountDAO.insert(dto);
        System.out.println("은행계좌 등록 완료: " + dto);
    }


    // 3. 예산 리포트 조회
    public List<ReportDTO> showBudgetReport(String yearMonth) throws SQLException {
        List<ReportDTO> report = reportDAO.selectBudgetReport(yearMonth);
        return report;
    }

    // 4. 계정 사용 중지 (Soft Delete) 기능
    public void deleteAccount(String accountId) throws SQLException {
        int result = accountDAO.softDeleteById(accountId);
        if (result == 0) {
            throw new IllegalArgumentException("계정 사용 중지 실패: 해당 ID를 찾을 수 없습니다.");
        }
    }

    // 5. 계정 복구 (Restore) 기능 추가
    public void restoreAccount(String accountId) throws SQLException {
        int result = accountDAO.restoreById(accountId);
        if (result == 0) {
            throw new IllegalArgumentException("계정 복구 실패: 해당 ID를 찾을 수 없습니다.");
        }
    }

    // ★ 6. 활성 계정 목록 조회 기능 (중복 제거 및 통합)
    public List<AccountDTO> getAllActiveAccounts() throws SQLException {
        return accountDAO.findAll();
    }

    // ★ 7. 비활성 계정 목록 조회 기능
    public List<AccountDTO> getAllDeactivatedAccounts() throws SQLException {
        return accountDAO.findDeactivated();
    }

    // ★ 8. 은행 계좌 정보 조회 기능 (Controller 호출 메서드, throws SQLException 추가)
    public List<BankAccountDTO> selectBankAccountInfo() throws SQLException {
        // BankAccountDAO.findAll() 메서드 호출을 위해 throws SQLException 추가
        return bankAccountDAO.findAll(); // BankAccountDAO에 findAll이 있다고 가정
    }
}