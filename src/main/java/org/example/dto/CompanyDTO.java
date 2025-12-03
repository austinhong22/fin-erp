package org.example.dto;

import java.time.LocalDateTime;

// 회사 데이터 모델
public class CompanyDTO {

    private String id;            // 회사 ID (UUID)
    private String name;          // 회사명
    private String businessNo;    // 사업자번호
    private LocalDateTime createdAt; // 생성일자(DB에서 NOW())

    public CompanyDTO(String id, String name, String businessNo, LocalDateTime createdAt) {
        this.id = id;
        this.name = name;
        this.businessNo = businessNo;
        this.createdAt = createdAt;
    }

    // getter/setter
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public String getBusinessNo() { return businessNo; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "CompanyDTO{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", businessNo='" + businessNo + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
