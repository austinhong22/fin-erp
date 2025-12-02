package org.example.dto;

public class BankAccountDTO {
    private String id;
    private String companyId;
    private String bankName;
    private String accountNo;
    private String description;

    public String getId() {return id;}

    public void setId(String id) {this.id = id;}

    public String getCompanyId() {return companyId;}

    public void setCompanyId(String companyId) {this.companyId = companyId;}

    public String getBankName() {return bankName;}

    public void setBankName(String bankName) {this.bankName = bankName;}

    public String getAccountNo() {return accountNo;}

    public void setAccountNo(String accountNo) {this.accountNo = accountNo;
    }

    public String getDescription() {return description;}

    public void setDescription(String description) {this.description = description;}


    @Override
    public String toString() {
        return "BankAccountDTO{" +
                "id='" + id + '\'' +
                ", companyId='" + companyId + '\'' +
                ", bankName='" + bankName + '\'' +
                ", accountNo='" + accountNo + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
