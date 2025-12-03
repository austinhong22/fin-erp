package org.example.dto;

import java.math.BigDecimal;

public class ReportDTO {
    private String deptName;//부서명
    private String accountName;//계정명
    private BigDecimal budgetAmount;//예산액
    private BigDecimal expenseAmount;//지출액
    private BigDecimal balance;//잔액

    public ReportDTO(String dept, String account, BigDecimal budget, BigDecimal expense, BigDecimal balance) {
        this.deptName = dept;
        this.accountName = account;
        this.budgetAmount = budget;
        this.expenseAmount = expense;
        this.balance = balance;
    }


    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public BigDecimal getBudgetAmount() {
        return budgetAmount;
    }

    public void setBudgetAmount(BigDecimal budgetAmount) {
        this.budgetAmount = budgetAmount;
    }

    public BigDecimal getExpenseAmount() {
        return expenseAmount;
    }

    public void setExpenseAmount(BigDecimal expenseAmount) {
        this.expenseAmount = expenseAmount;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
