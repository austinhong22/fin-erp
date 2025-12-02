package org.example.service;

import org.example.dao.CompanyDao;
import org.example.dao.DepartmentDao;
import org.example.dto.Company;
import org.example.dto.Department;

import java.util.List;

public class DepartmentService {

    private final DepartmentDao departmentDao = new DepartmentDao();
    private final CompanyDao companyDao = new CompanyDao(); // 회사 존재 여부 확인용

    /**
     * 부서 등록 서비스
     * @param companyId 회사 ID
     * @param name 부서명
     * @param code 부서코드
     */
    public Department registerDepartment(String companyId, String name, String code) {

        // 1) 회사 존재 여부 체크
        Company company = companyDao.findById(companyId);
        if (company == null) {
            throw new IllegalArgumentException("❌ 존재하지 않는 회사ID입니다.");
        }

        // 2) 부서명 검증
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("❌ 부서명은 반드시 입력해야 합니다.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("❌ 부서명은 100자를 넘을 수 없습니다.");
        }

        // 3) 코드 검증
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("❌ 부서 코드는 반드시 입력해야 합니다.");
        }
        if (!code.matches("[A-Za-z]{2,}")) {
            throw new IllegalArgumentException("❌ 부서 코드는 2글자 이상 영문만 가능합니다. (예: DEV, HR)");
        }

        // 4) DTO 생성 (id는 DAO가 넣음)
        Department dept = new Department(companyId, code, name);

        // 5) 저장
        departmentDao.addDepartment(dept);

        return dept;
    }

    // 전체 조회
    public List<Department> getAllDepartments() {
        return departmentDao.findAll();
    }

    // 특정 회사의 부서 조회
    public List<Department> getDepartmentsByCompanyId(String companyId) {
        return departmentDao.findByCompanyId(companyId);
    }

    // ID로 조회
    public Department getDepartmentById(String id) {
        return departmentDao.findById(id);
    }

    // 삭제
    public boolean deleteDepartment(String id) {
        return departmentDao.deleteDepartment(id);
    }
}
