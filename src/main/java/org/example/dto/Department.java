package org.example.dto;

import java.util.UUID;

public class Department {
    private String id;
    private  String companyId;
    private String name;
    private String code;
    //기본 생성자
    public Department(){
        this.id = UUID.randomUUID().toString();
    }
    //전체 생성자
    public Department(String id, String companyId, String name, String code){
        this.id = id;
        this.companyId = companyId;
        this.name = name;
        this.code = code;
    }

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

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
    //자동출력문
    @Override
    public String toString() {
        return "Department{" +
                "id='" + id + '\'' +
                ", companyId='" + companyId + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                '}';
    }
}