package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dto.Company;

import java.util.List;

public class CompanyService {

    private final CompanyDao companyDao = new CompanyDao();

    // 회사 등록
    public Company registerCompany(String name, String businessNo) {

        // 1) 회사명 검증
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("회사명은 반드시 입력해야 합니다.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("회사명은 100자를 넘을 수 없습니다.");
        }

        // 2) 사업자번호 검증
        if (businessNo == null || businessNo.isBlank()) {
            throw new IllegalArgumentException("사업자번호는 반드시 입력해야 합니다.");
        }

        String num = businessNo.replace("-", ""); // 하이픈 제거

        if (!num.matches("\\d{10}")) {
            throw new IllegalArgumentException("사업자번호는 숫자 10자리여야 합니다. (예: 123-45-67890)");
        }

        // 3) 중복사업자번호 검사
        for (Company c : companyDao.findAll()) {
            String existing = c.getBusinessNo().replace("-", "");
            if (existing.equals(num)) {
                throw new IllegalArgumentException("이미 등록된 사업자번호입니다.");
            }
        }

        // 4) 생성 & 저장
        Company company = new Company(name, num);
        companyDao.addCompany(company);

        return company;
    }

    // 전체 조회
    public List<Company> getAllCompanies() {
        return companyDao.findAll();
    }

    // ID로 조회
    public Company getCompanyById(String id) {
        return companyDao.findById(id);
    }

    // 삭제
    public boolean deleteCompany(String id) {
        return companyDao.deleteCompany(id);
    }
}
