package org.example.service;

import org.example.dao.AccountDAO;
import org.example.dao.BankAccountDAO;
import org.example.dto.AccountDTO;
import org.example.dto.AccountType;
import org.example.dto.BankAccountDTO;

import java.util.UUID;

public class FinanceService {
    private final AccountDAO accountDAO = new AccountDAO();
    private final BankAccountDAO bankAccountDAO = new BankAccountDAO();
    private static final String DEFAULT_COMPANY_ID = "CP-001";//회사 ID 고정값



    public void registerAccount(String code, String name, String type) {

        AccountDTO dto = new AccountDTO();
        dto.setId(UUID.randomUUID().toString());//ID 자동생성
        dto.setCompanyId(DEFAULT_COMPANY_ID);
        dto.setCode(code);
        dto.setName(name);
        dto.setType(AccountType.valueOf(type.toUpperCase()));

        accountDAO.insert(dto);
        System.out.println("계정과목 등록 완료: " + dto);
    }

    public void registerBankAccount(String bankName, String accountNo, String description) {

        BankAccountDTO dto = new BankAccountDTO();
        dto.setId(UUID.randomUUID().toString());
        dto.setCompanyId(DEFAULT_COMPANY_ID);   // 회사 ID 고정값
        dto.setBankName(bankName);
        dto.setAccountNo(accountNo);
        dto.setDescription(description);

        bankAccountDAO.insert(dto);
        System.out.println("은행계좌 등록 완료: " + dto);
    }
}