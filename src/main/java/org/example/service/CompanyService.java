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

        CompanyDTO dto = new CompanyDTO(
                UUID.randomUUID().toString(), // UUID 생성
                name,
                businessNo,
                null  // created_at은 DB NOW() 사용
        );

        companyDAO.insert(dto);
        return dto;
    }

    // 전체 조회
    public List<CompanyDTO> getAllCompanies() {
        return companyDAO.selectAll();
    }

    // ID로 조회
    public CompanyDTO getById(String id) {
        return companyDAO.selectById(id);
    }
}
