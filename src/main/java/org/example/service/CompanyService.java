package org.example.service;

import org.example.dao.CompanyDAO;
import org.example.dto.CompanyDTO;

import java.util.List;
import java.util.UUID;

// 회사 비즈니스 로직 서비스
public class CompanyService {

    private final CompanyDAO companyDAO = new CompanyDAO();

    // 회사 등록
    public CompanyDTO registerCompany(String name, String businessNo) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("회사명은 비워둘 수 없습니다.");
        }
        if (businessNo == null || businessNo.isBlank()) {
            throw new IllegalArgumentException("사업자번호는 비워둘 수 없습니다.");
        }

        CompanyDTO dto = new CompanyDTO(
                UUID.randomUUID().toString(),
                name,
                businessNo,
                null // created_at은 DB NOW()로 생성
        );

        companyDAO.insert(dto);
        return dto;
    }

    // 회사 수정
    public boolean updateCompany(String id, String newName, String newBusinessNo) {

        CompanyDTO existing = companyDAO.selectById(id);
        if (existing == null) {
            throw new IllegalArgumentException("존재하지 않는 회사입니다: " + id);
        }

        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("회사명은 비워둘 수 없습니다.");
        }
        if (newBusinessNo == null || newBusinessNo.isBlank()) {
            throw new IllegalArgumentException("사업자번호는 비워둘 수 없습니다.");
        }

        existing.setName(newName);
        existing.setBusinessNo(newBusinessNo);

        int rows = companyDAO.update(existing);
        return rows > 0;
    }

    // 전체 조회
    public List<CompanyDTO> getAllCompanies() {
        return companyDAO.selectAll();
    }

    // ID 조회
    public CompanyDTO getById(String id) {
        return companyDAO.selectById(id);
    }
}
