package org.example.dao;

import org.example.dto.Company;

import java.util.ArrayList;
import java.util.List;

public class CompanyDao {
    // 메모리 저장소 (DB 대신)
    private final List<Company> companyList = new ArrayList<>();

    // CREATE (등록)
    public void addCompany(Company company) {
        companyList.add(company);
    }

    // READ (전체 조회)
    public List<Company> findAll() {
        return companyList;
    }

    // READ (ID로 조회)
    public Company findById(String id) {
        for (Company c : companyList) {
            if (c.getId().equals(id)) {
                return c;
            }
        }
        return null;
    }

    // UPDATE (회사 정보 수정)
    public boolean updateCompany(Company updated) {
        for (int i = 0; i < companyList.size(); i++) {
            if (companyList.get(i).getId().equals(updated.getId())) {
                companyList.set(i, updated);
                return true;
            }
        }
        return false;
    }

    // DELETE
    public boolean deleteCompany(String id) {
        return companyList.removeIf(c -> c.getId().equals(id));
    }
}