package org.example.view;

import org.example.dao.CompanyDao;
import org.example.dao.DepartmentDao;
import org.example.dto.Company;
import org.example.dto.Department;

import java.util.List;

public class DepartmentService {
    private final DepartmentDao departmentDao = new DepartmentDao();
    private final CompanyDao companyDao = new CompanyDao(); // 회사 검증용

    // 부서 등록
    public Department registerDepartment(String companyId, String name, String code) {

        Company company = companyDao.findById(companyId);//회사 검증
        if (company == null) {
            throw new IllegalArgumentException("존재하지 않는 회사ID입니다.");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("부서명은 반드시 입력해야 합니다.");
        }
        if (name.length() > 100) {
            throw new IllegalArgumentException("부서명은 100자를 초과할 수 없습니다.");
        }
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException("부서 코드는 반드시 입력해야 합니다.");
        }
        if (code.length() < 2) {
            throw new IllegalArgumentException("부서 코드는 최소 2자리 이상이어야 합니다.");
        }
        if (!code.matches("[A-Za-z]+")) {
            throw new IllegalArgumentException("부서 코드는 영어 알파벳만 사용할 수 있습니다.");
        }
        // 대문자로 통일
        String upperCode = code.toUpperCase();

        Department dept = new Department();
        dept.setCompanyId(companyId);
        dept.setName(name);
        dept.setCode(upperCode);

        departmentDao.addDepartment(dept);
        return dept;
    }

    // 전체 부서 조회
    public List<Department> getAllDepartments() {
        return departmentDao.findAll();
    }

    // 특정 회사의 부서 조회
    public List<Department> getDepartmentsByCompanyId(String companyId) {
        return departmentDao.findByCompanyId(companyId);
    }

    // 부서 ID로 조회
    public Department getDepartmentById(String id) {
        return departmentDao.findById(id);
    }

    // 부서 삭제
    public boolean deleteDepartment(String id) {
        return departmentDao.deleteDepartment(id);
    }
}
