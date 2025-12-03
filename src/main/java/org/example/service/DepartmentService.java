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

        // 회사 존재 검증
        CompanyDTO company = companyDAO.selectById(companyId);
        if (company == null) {
            throw new IllegalArgumentException("존재하지 않는 회사입니다: " + companyId);
        }

        // 입력 검증
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("부서명은 비워둘 수 없습니다.");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("부서코드는 비워둘 수 없습니다.");
        }

        // DTO 생성
        DepartmentDTO dto = new DepartmentDTO(
                UUID.randomUUID().toString(),
                companyId,
                name,
                code.toUpperCase()
        );

        departmentDAO.insert(dto);
        return dto;
    }

    // 부서 수정
    public boolean updateDepartment(String deptId, String newName, String newCode) {

        DepartmentDTO existing = departmentDAO.selectById(deptId);
        if (existing == null) {
            throw new IllegalArgumentException("존재하지 않는 부서입니다: " + deptId);
        }

        if (newName == null || newName.isBlank()) {
            throw new IllegalArgumentException("부서명은 비워둘 수 없습니다.");
        }
        if (newCode == null || newCode.isBlank()) {
            throw new IllegalArgumentException("부서코드는 비워둘 수 없습니다.");
        }

        existing.setName(newName);
        existing.setCode(newCode.toUpperCase());

        int rows = departmentDAO.update(existing);
        return rows > 0;
    }

    // 회사의 전체 부서 목록 조회
    public List<DepartmentDTO> getDepartmentsByCompanyId(String companyId) {
        return departmentDAO.selectByCompanyId(companyId);
    }
}
