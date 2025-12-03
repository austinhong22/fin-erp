package org.example.dto;

// 부서 데이터 모델
public class DepartmentDTO {

    private String id;         // 부서 ID (UUID)
    private String companyId;  // 회사 ID (FK)
    private String name;       // 부서명
    private String code;       // 부서코드

    public DepartmentDTO(String id, String companyId, String name, String code) {
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.code = code;
    }

    // getter/setter
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
