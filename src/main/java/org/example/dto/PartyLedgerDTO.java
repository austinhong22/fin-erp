package org.example.dto;

import java.math.BigDecimal;

public class PartyLedgerDTO {

    // 1. 날짜 (JournalEntry에서 가져옴)
    private String entryDate;
    // 2. 적요 (JournalEntry의 description 사용)
    private String description;
    // 3. 계정과목 이름 (GLAccount 테이블 조인 결과)
    private String glAccountName;
    // 4. 금액 (JournalLine에서 가져옴)
    private BigDecimal debitAmount;
    private BigDecimal creditAmount;

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setGlAccountName(String glAccountName) {
        this.glAccountName = glAccountName;
    }

    public void setDebitAmount(BigDecimal debitAmount) {
        this.debitAmount = debitAmount;
    }

    public void setCreditAmount(BigDecimal creditAmount) {
        this.creditAmount = creditAmount;
    }

    public String getDescription() {
        return description;
    }

    public String getGlAccountName() {
        return glAccountName;
    }

    public BigDecimal getDebitAmount() {
        return debitAmount;
    }

    public BigDecimal getCreditAmount() {
        return creditAmount;
    }

    // --- Getter & Setter, toString 메서드 추가 ---
}