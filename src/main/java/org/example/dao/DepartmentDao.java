package org.example.dao;

import org.example.dto.Department;
import java.util.*;
import java.util.stream.Collectors;

public class DepartmentDao {

    // Map을 사용해서 id → Department 저장 구조로 개선
    private static final Map<String, Department> departmentStore = new HashMap<>();

    // ID는 DAO가 생성한다
    private String generateId() {
        return UUID.randomUUID().toString();
    }

    // CREATE
    public void addDepartment(Department dept) {
        String id = generateId();
        dept.setId(id);   // DTO에 id 세팅 (DAO 전용)
        departmentStore.put(id, dept);
    }

    // READ - 전체 조회
    public List<Department> findAll() {
        return new ArrayList<>(departmentStore.values());
    }

    // READ - 특정 회사의 부서들 조회
    public List<Department> findByCompanyId(String companyId) {
        return departmentStore.values().stream()
                .filter(d -> d.getCompanyId().equals(companyId))
                .collect(Collectors.toList());
    }

    // READ - 하나 조회
    public Department findById(String id) {
        return departmentStore.get(id);
    }

    // DELETE
    public boolean deleteDepartment(String id) {
        return departmentStore.remove(id) != null;
    }
}
