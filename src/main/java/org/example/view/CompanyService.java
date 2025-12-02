package org.example.view;

import org.example.dao.CompanyDao;
import org.example.dto.Company;

import java.util.List;

public class CompanyService {
    private final CompanyDao companyDao = new CompanyDao();

    // 회사 등록
    public Company registerCompany(String name, String businessNo) {
        //회사명 null X
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("회사명은 반드시 입력해야 합니다.");
        }
        // 이름 길이 제한 (DB varchar(100))
        if (name.length() > 100) {
            throw new IllegalArgumentException("회사명은 100자를 넘을 수 없습니다.");
        }
        // 회사명 중복 검증
        for (Company c : companyDao.findAll()) {
            if (c.getName().equals(name)) {
                throw new IllegalArgumentException("이미 같은 이름의 회사가 존재합니다.");
            }
        }
        if (businessNo == null || businessNo.isBlank()) {
            throw new IllegalArgumentException("사업자번호는 반드시 입력해야 합니다.");
        }
        // 숫자만 남기기 위한 처리
        String num = businessNo.replace("-", "");
        // 숫자형식 + 길이 체크
        if (!num.matches("\\d{10}")) {
            throw new IllegalArgumentException("사업자번호는 숫자 10자리여야 합니다. 예: 123-45-67890");
        }
        // 중복 사업자번호 방지
        for (Company c : companyDao.findAll()) {
            String existing = c.getBusinessNo().replace("-", "");
            if (existing.equals(num)) {
                throw new IllegalArgumentException("이미 등록된 사업자번호입니다.");
            }
        }
        // 여기에서 나중에 검증 로직도 넣을 수 있음 (공백 체크 등)
        Company company = new Company(name, businessNo);
        companyDao.addCompany(company);
        return company;
    }
    // 회사 전체 조회
    public List<Company> getAllCompanies() {
        return companyDao.findAll();
    }

    // 회사 ID로 조회
    public Company getCompanyById(String id) {
        return companyDao.findById(id);
    }

    // 회사 삭제
    public boolean deleteCompany(String id) {
        return companyDao.deleteCompany(id);
    }

}
