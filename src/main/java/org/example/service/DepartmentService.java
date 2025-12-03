package org.example.service;

import org.example.dao.CompanyDAO;
import org.example.dao.DepartmentDAO;
import org.example.dto.CompanyDTO;
import org.example.dto.DepartmentDTO;

import java.util.List;
import java.util.UUID;

// 부서 비즈니스 로직 서비스
public class DepartmentService {

    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final CompanyDAO companyDAO = new CompanyDAO();

    // 부서 등록
    public DepartmentDTO registerDepartment(String companyId, String name, String code) {

        // 회사 존재 여부 확인
        CompanyDTO company = companyDAO.selectById(companyId);
        if (company == null) {
            throw new IllegalArgumentException("존재하지 않는 회사입니다.");
        }

        // 부서 객체 생성
        DepartmentDTO dto = new DepartmentDTO(
                UUID.randomUUID().toString(),
                companyId,
                name,
                code.toUpperCase()
        );

        departmentDAO.insert(dto);
        return dto;
    }

    // 회사의 부서 목록 조회
    public List<DepartmentDTO> getDepartmentsByCompanyId(String companyId) {
        return departmentDAO.selectByCompanyId(companyId);
    }
}
