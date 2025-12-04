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

    // 1. 계정과목 등록 (Create)
    public void registerAccount(String code, String name, AccountType type) throws SQLException { // ★ throws SQLException 추가
        AccountDTO dto = new AccountDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setCompanyId(DEFAULT_COMPANY_ID);
        dto.setCode(code);
        dto.setName(name);
        dto.setType(type);

        accountDAO.insert(dto); // throws SQLException
        System.out.println("계정과목 등록 완료: " + dto);
    }

    // 2. 은행 계좌 등록 (Create)
    public void registerBankAccount(String bankName, String accountNo, String accountAlias, String description) throws SQLException { // ★ throws SQLException 추가
        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setCompanyId(DEFAULT_COMPANY_ID);
        dto.setBankName(bankName);
        dto.setAccountNo(accountNo);
        dto.setAccountAlias(accountAlias);
        dto.setDescription(description);

        bankAccountDAO.insert(dto); // throws SQLException
        System.out.println("은행계좌 등록 완료: " + dto);
    }


    // 3. 예산 리포트 조회
    public List<ReportDTO> showBudgetReport(String yearMonth) throws SQLException { // ★ throws SQLException 추가
        List<ReportDTO> report = reportDAO.selectBudgetReport(yearMonth);
        return report;
    }

    // ===============================================
    // ★ 4. 계정과목 사용 중지 (Soft Delete) 기능 추가
    // ===============================================
    public void deleteAccount(String accountId) throws SQLException { // ★ throws SQLException 추가
        int result = accountDAO.softDeleteById(accountId);
        if (result == 0) {
            throw new IllegalArgumentException("계정과목 사용 중지 실패: 해당 ID를 찾을 수 없습니다.");
        }
    }


    // ===============================================
    // ★ 5. 활성 계정과목 목록 조회 기능 추가
    // ===============================================
    public List<AccountDTO> getAllActiveAccounts() throws SQLException { // ★ throws SQLException 추가
        // DAO가 이미 is_active = 'Y'로 필터링된 목록을 가져옵니다.
        return accountDAO.findAll();
    }

    public List<BankAccountDTO> selectBankAccountInfo() {
        List<BankAccountDTO> bankAccountDTO = bankAccountDAO.findAll();

        return bankAccountDTO;
    }
}