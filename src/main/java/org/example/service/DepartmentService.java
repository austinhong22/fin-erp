package org.example.service;

import org.example.dao.DepartmentDAO;
import org.example.dto.DepartmentDTO;

import java.util.List;
import java.sql.SQLException;
import java.util.UUID;

public class DepartmentService {

    private final DepartmentDAO dao = new DepartmentDAO();
    private static final String DEFAULT_COMPANY_ID = "CP-001"; // 고정된 회사 ID

    // 💡 1. 부서 등록 로직 (비즈니스 로직 처리)
    public void register(String name, String code) {

        // 1. DTO 생성 및 값 설정
        DepartmentDTO dto = new DepartmentDTO();

        // 2. 비즈니스 로직: ID 직접 생성 (UUID)
        dto.setId(UUID.randomUUID().toString());

        // 3. 비즈니스 로직: 회사 ID 고정
        dto.setCompanyId(DEFAULT_COMPANY_ID);

        // 4. 입력 값 설정
        dto.setName(name);
        dto.setCode(code);

        // 5. DAO 호출 (DB 저장)

        int result = dao.insert(dto);
        if (result > 0) {
            System.out.println("부서 등록 성공: " + dto.getName() + "(" + dto.getCode() + ")");
        } else {
            // 이 경우 일반적으로 DB 에러가 발생하며 SQLException이 발생하지만,
            // 0이 반환될 경우를 대비
            System.out.println(" 부서 등록 실패: DB 반영 안됨.");
        }

    }

    // 💡 2. 부서 전체 조회
    public List<DepartmentDTO> getAllDepartments() {
        try {
            return dao.selectAll();
        } catch (SQLException e) {
            // DB 전용 예외를 애플리케이션 예외(Runtime)로 변환
            System.err.println("DB 조회 오류 발생: " + e.getMessage());
            throw new RuntimeException("부서 정보 조회 실패", e); // 💡 RuntimeException으로 래핑
        }
    }
}
