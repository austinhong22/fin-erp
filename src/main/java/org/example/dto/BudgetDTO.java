package org.example.dto;

import java.math.BigDecimal;

public class BudgetDTO {

    private String id;
    private String companyId;
    private String departmentId;
    private String glAccountId;
    private String yearMonth;
    private BigDecimal budgetAmount; // ★ long -> BigDecimal 변경

    public BudgetDTO(String id, String companyId, String departmentId,
                     String glAccountId, String yearMonth, BigDecimal budgetAmount) { // ★ BigDecimal
        this.id = id;
        this.companyId = companyId;
        this.departmentId = departmentId;
        this.glAccountId = glAccountId;
        this.yearMonth = yearMonth;
        this.budgetAmount = budgetAmount;
    }

    public String getId() { return id; }
    public String getCompanyId() { return companyId; }
    public String getDepartmentId() { return departmentId; }
    public String getGlAccountId() { return glAccountId; }
    public String getYearMonth() { return yearMonth; }
    public BigDecimal getBudgetAmount() { return budgetAmount; } // ★ BigDecimal

    // setter 생략

    @Override
    public String toString() {
        return "BudgetDTO{" +
                "id='" + id + '\'' +
                ", companyId='" + companyId + '\'' +
                ", departmentId='" + departmentId + '\'' +
                ", glAccountId='" + glAccountId + '\'' +
                ", yearMonth='" + yearMonth + '\'' +
                ", budgetAmount=" + budgetAmount +
                '}';
    }
}