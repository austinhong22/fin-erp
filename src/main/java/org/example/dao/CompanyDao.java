package org.example.dao;

import org.example.dto.Company;

import java.util.*;

public class CompanyDao {

    private static final Map<String, Company> companyStore = new HashMap<>();

    private String generateId() {
        return UUID.randomUUID().toString();
    }

    public void addCompany(Company company) {
        String id = generateId();
        company.setId(id);
        companyStore.put(id, company);
    }

    public Company findById(String id) {
        return companyStore.get(id);
    }

    public List<Company> findAll() {
        return new ArrayList<>(companyStore.values());
    }

    public boolean deleteCompany(String id) {
        return companyStore.remove(id) != null;
    }
}
