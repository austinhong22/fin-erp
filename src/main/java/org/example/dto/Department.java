package org.example.dto;

public class Department {

    private String id;            // DAO에서만 설정할 ID
    private final String companyId;  // 참조할 회사 ID (변경 불가)
    private final String code;    // 부서코드 (DEV, HR) - 변경 불가
    private String name;          // 부서명 (수정 가능)

    // 생성자: 필수 정보만 입력
    public Department(String companyId, String code, String name) {
        this.companyId = companyId;
        this.code = code.toUpperCase(); // 코드 대문자 통일
        this.name = name;
    }

    // ID는 DAO에서 저장 시 넣어줌
    public void setId(String id) {
        this.id = id;
    }

    // Getter
    public String getId() { return id; }
    public String getCompanyId() { return companyId; }
    public String getCode() { return code; }
    public String getName() { return name; }

    // Setter (부서명만 수정 가능)
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Department{" +
                "id='" + id + '\'' +
                ", companyId='" + companyId + '\'' +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
