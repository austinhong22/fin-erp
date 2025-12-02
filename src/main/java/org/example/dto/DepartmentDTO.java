package org.example.dto;

public class DepartmentDTO {
    // DB 테이블 컬럼과 동일한 필드
    private String id;
    private String companyId;
    private String name;
    private String code;

    // --- Getter & Setter ---
    public String getId() { return id; }

    public void setId(String id) { this.id = id; }

    public String getCompanyId() { return companyId; }

    public void setCompanyId(String companyId) { this.companyId = companyId; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public String getCode() { return code; }

    public void setCode(String code) { this.code = code; }

    @Override
    public String toString() {
        return "DepartmentDTO{" +
                "id='" + id + '\'' +
                ", companyId='" + companyId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}