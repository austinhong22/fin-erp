package org.example.service;

import org.example.config.AppConfig;
import org.example.dao.CompanyDAO;
import org.example.dao.DepartmentDAO;
import org.example.dto.CompanyDTO;
import org.example.dto.DepartmentDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

// 부서 비즈니스 로직 서비스 (최종본)
public class DepartmentService {

    private final DepartmentDAO departmentDAO = new DepartmentDAO();
    private final CompanyDAO companyDAO = new CompanyDAO();

    // =========================
    // 1. 부서 등록
    // =========================
    // View/Controller에서는 회사ID 안 받음 (CP-001 고정)
    public DepartmentDTO registerDepartment(String name, String code) throws SQLException {

        // 회사 존재 검증 (CP-001)
        CompanyDTO company = companyDAO.selectById(AppConfig.COMPANY_ID);
        if (company == null) {
            throw new IllegalStateException("기본 회사가 존재하지 않습니다: " + AppConfig.COMPANY_ID);
        }

        // 입력값 검증
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("부서명은 비워둘 수 없습니다.");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("부서코드는 비워둘 수 없습니다.");
        }

        // 코드 대문자 통일
        String upperCode = code.toUpperCase();

        // 코드 중복 체크
        if (departmentDAO.selectByCode(upperCode) != null) {
            throw new IllegalArgumentException("이미 존재하는 부서코드입니다: " + upperCode);
        }

        // DTO 생성
        DepartmentDTO dto = new DepartmentDTO(
                UUID.randomUUID().toString(),
                AppConfig.COMPANY_ID,
                name,
                upperCode
        );

        departmentDAO.insert(dto);
        return dto;
    }

    // =========================
    // 2. 부서 수정 (옵션)
    // =========================
    public boolean updateDepartment(String deptId, String newName, String newCode) throws SQLException {

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

        String upperCode = newCode.toUpperCase();

        // 코드 변경하는 경우에만 중복체크
        DepartmentDTO byCode = departmentDAO.selectByCode(upperCode);
        if (byCode != null && !byCode.getId().equals(deptId)) {
            throw new IllegalArgumentException("이미 사용 중인 부서코드입니다: " + upperCode);
        }

        existing.setName(newName);
        existing.setCode(upperCode);

        int rows = departmentDAO.update(existing);
        return rows > 0;
    }

    // =========================
    // 3. 회사별 부서 목록 조회
    // =========================
    public List<DepartmentDTO> getDepartmentsByCompanyId(String companyId) throws SQLException {
        return departmentDAO.selectByCompanyId(companyId);
    }

    public List<DepartmentDTO> getDepartments(String companyId) throws SQLException {
        return departmentDAO.selectByCompanyId(AppConfig.COMPANY_ID);
    }
    // ===============================================
    // 부서 삭제 기능 (Delete) - Soft Delete 로직 연결
    // ===============================================
        public void deleteDepartment(String deptId) throws SQLException {
        try {
            // DAO의 softDeleteById 호출 (SQLException 던짐)
            int result = departmentDAO.softDeleteById(deptId);

            if (result == 0) {
                // 0을 반환했다면 ID를 찾지 못했거나 이미 비활성화된 상태
                throw new IllegalArgumentException("부서 삭제 실패: 해당 ID를 찾을 수 없습니다.");
            }
        } catch (IllegalArgumentException e) {
            // 비즈니스 오류는 그대로 던짐
            throw e;
        } catch (SQLException e) {
            // DB 오류는 시스템 오류로 변환
            throw new IllegalStateException("부서 삭제 중 DB 오류 발생: " + e.getMessage(), e);
        }
    }
}
