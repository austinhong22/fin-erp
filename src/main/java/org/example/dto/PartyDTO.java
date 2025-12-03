package org.example.dto;

public class PartyDTO {

    private String id;
    private String companyId; // DB 테이블 구조에 포함되므로 필드에 추가
    private String name;
    private String type; // 거래처 유형: CUSTOMER(고객) 또는 VENDOR(공급업체)
    private String contact;
    private String registrationNumber; // 사업자 등록 번호 (DB 스키마에 포함)

    // --- Getter & Setter ---

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    @Override
    public String toString() {
        return "PartyDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", contact='" + contact + '\'' +
                ", registrationNumber='" + registrationNumber + '\'' +
                '}';
    }
}