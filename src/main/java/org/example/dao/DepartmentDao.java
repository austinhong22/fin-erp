package org.example.dao;

import org.example.dto.Department;

import java.util.ArrayList;
import java.util.List;

public class DepartmentDao {

    // 메모리 저장소
    private final List<Department> departmentList = new ArrayList<>();

    // CREATE
    public void addDepartment(Department dept) {
        departmentList.add(dept);
    }

    // READ (전체 조회)
    public List<Department> findAll() {
        return departmentList;
    }

    // READ (ID로 조회)
    public Department findById(String id) {
        for (Department d : departmentList) {
            if (d.getId().equals(id)) return d;
        }
        return null;
    }

    // READ (특정 회사의 부서 조회)
    public List<Department> findByCompanyId(String companyId) {
        List<Department> result = new ArrayList<>();
        for (Department d : departmentList) {
            if (d.getCompanyId().equals(companyId)) {
                result.add(d);
            }
        }
        return result;
    }

    // UPDATE
    public boolean updateDepartment(Department updated) {
        for (int i = 0; i < departmentList.size(); i++) {
            if (departmentList.get(i).getId().equals(updated.getId())) {
                departmentList.set(i, updated);
                return true;
            }
        }
        return false;
    }

    // DELETE
    public boolean deleteDepartment(String id) {
        return departmentList.removeIf(d -> d.getId().equals(id));
    }
}
