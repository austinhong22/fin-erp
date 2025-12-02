package org.example.dto;

import java.util.ArrayList;
import java.util.List;

public class JournalEntryDTO {

    private String id;
    private String entryDate;
    private String description;

    private String partyId;
    private String bankAccountId;

    // 전표 1장에는 여러 개의 상세 보유
    private List<JournalLineDTO> lines = new ArrayList<>();

    // 3라인 추가 편의 메서드 (이거 있으면 편함)
    public void addLine(JournalLineDTO line) {
        this.lines.add(line);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEntryDate() {
        return entryDate;
    }

    public void setEntryDate(String entryDate) {
        this.entryDate = entryDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
    }

    public String getBankAccountId() {
        return bankAccountId;
    }

    public void setBankAccountId(String bankAccountId) {
        this.bankAccountId = bankAccountId;
    }

    public List<JournalLineDTO> getLines() {
        return lines;
    }

    public void setLines(List<JournalLineDTO> lines) {
        this.lines = lines;
    }

    @Override
    public String toString() {
        return "JournalEntryDTO{" +
                "id='" + id + '\'' +
                ", entryDate='" + entryDate + '\'' +
                ", description='" + description + '\'' +
                ", partyId='" + partyId + '\'' +
                ", bankAccountId='" + bankAccountId + '\'' +
                ", lines=" + lines +
                '}';
    }
}
