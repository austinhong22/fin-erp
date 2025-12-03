package org.example.service;


import org.example.dao.PartyDAO;
import org.example.dto.PartyDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PartyService {

    private final PartyDAO dao = new PartyDAO();
    private static final String DEFAULT_COMPANY_ID = "CP-001"; // 고정된 회사 ID

    // 💡 1. 거래처 등록 로직 (C)
    public void registerParty(String name, String type, String contact, String regNo) throws SQLException {

        PartyDTO dto = new PartyDTO();

        // 1. 비즈니스 로직: ID 직접 생성 (UUID)
        dto.setId(UUID.randomUUID().toString());

        // 2. 비즈니스 로직: 회사 ID 고정
        dto.setCompanyId(DEFAULT_COMPANY_ID);

        // 3. 비즈니스 로직: type을 대문자로 변환하여 저장 (Tip 반영)
        dto.setType(type.toUpperCase());

        // 4. 입력 값 설정
        dto.setName(name);
        dto.setContact(contact);
        dto.setRegistrationNumber(regNo);

        // 5. DAO 호출 (DB 저장)
        int result = dao.insert(dto);
        if (result > 0) {
            System.out.println("거래처 등록 성공: " + dto.getName() + " (" + dto.getType() + ")");
        } else {
            System.out.println("거래처 등록 실패: DB 반영 안됨.");
        }
    }

    // 💡 2. 타입별 거래처 조회 로직 (R)
    public List<PartyDTO> getPartiesByType(String type) throws SQLException {

        // 1. 비즈니스 로직: 조회 타입도 대문자로 변환하여 DAO에 전달
        String searchType = type.toUpperCase();

        return dao.selectByType(searchType);
    }
}