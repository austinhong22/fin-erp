package model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Company {
    private String id;
    private String name;
    private String businessNo;
    private LocalDateTime createdAt;

    public Company() {
        this.id = UUID.randomUUID().toString();
        this.createdAt = LocalDateTime.now();
    }
public Company(String name,String bussiness_no){
    this.id = UUID.randomUUID().toString();
    this.name = name;
    this.businessNo = businessNo;
    this.createdAt = LocalDateTime.now();
}





















}
