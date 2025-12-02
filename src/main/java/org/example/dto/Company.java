package org.example.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public class Company {
    private String id;
    private String name;
    private String businessNo;
    private LocalDateTime createdAt;
    //기본 생성자
    public Company() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }
    //전체 생성자
    public Company(String name,String businessNo){
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.businessNo = businessNo;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBusinessNo() {
        return businessNo;
    }

    public void setBusinessNo(String businessNo) {
        this.businessNo = businessNo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    //자동출력문
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
