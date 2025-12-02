package org.example.dto;

import java.time.LocalDateTime;

public class Company {

    private String id;               // DAO에서 생성
    private final String name;       // 회사명
    private final String businessNo; // 사업자번호(고유)
    private final LocalDateTime createdAt; // 생성일자

    public Company(String name, String businessNo) {
        this.name = name;
        this.businessNo = businessNo;
        this.createdAt = LocalDateTime.now();
    }

    // DAO 전용
    public void setId(String id) {
        this.id = id;
    }

    // Getter
    public String getId() { return id; }
    public String getName() { return name; }
    public String getBusinessNo() { return businessNo; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    @Override
    public String toString() {
        return "Company{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", businessNo='" + businessNo + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
