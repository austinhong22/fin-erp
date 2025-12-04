package org.example.service;


import org.example.dao.PartyDAO;
import org.example.dto.PartyDTO;
import org.example.dto.PartyLedgerDTO;

import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class PartyService {

    private final PartyDAO dao = new PartyDAO();
    private static final String DEFAULT_COMPANY_ID = "CP-001"; // 고정된 회사 ID

    // 1. 거래처 등록 로직 (C)
    public void registerParty(String name, String type, String contact, String regNo) throws SQLException { // ★ throws SQLException 추가

        PartyDTO dto = new PartyDTO();

        // 1. 비즈니스 로직: ID 직접 생성 (UUID)
        dto.setId(UUID.randomUUID().toString());

        // 2. 비즈니스 로직: 회사 ID 고정
        dto.setCompanyId(DEFAULT_COMPANY_ID);

        // 3. 비즈니스 로직: type을 대문자로 변환하여 저장
        dto.setType(type.toUpperCase());

        // 4. 입력 값 설정
        dto.setName(name);
        dto.setContact(contact);
        dto.setRegistrationNumber(regNo);

        // 5. DAO 호출 (DB 저장)
        int result = dao.insert(dto); // DAO가 SQLException을 던지므로 Service도 던짐
        if (result > 0) {
            // 이 출력문은 Controller로 옮기는 것이 원칙이나, 현재 구조 유지를 위해 여기에 둡니다.
            System.out.println("거래처 등록 성공: " + dto.getName() + " (" + dto.getType() + ")");
        } else {
            // INSERT 실패는 DB 오류(SQLException)로 처리되므로, 0이 반환될 경우 비즈니스적 오류로 간주할 수 있음
            throw new IllegalStateException("거래처 등록 실패: DB 반영 실패");
        }
    }

    // 2. 타입별 거래처 조회 로직 (R)
    public List<PartyDTO> getPartiesByType(String type) throws SQLException { // ★ throws SQLException 추가

        // 1. 비즈니스 로직: 조회 타입도 대문자로 변환하여 DAO에 전달
        String searchType = type.toUpperCase();

        return dao.selectByType(searchType);
    }

    // 거래처 전체 목록 조회 기능 추가
    public List<PartyDTO> getAllParties() throws SQLException { // ★ throws SQLException 추가
        return dao.selectAll();
    }

    // 거래처 이름 키워드 검색 기능 추가
    public List<PartyDTO> searchParties(String keyword) throws SQLException { // ★ throws SQLException 추가
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new IllegalArgumentException("검색 키워드는 필수입니다.");
        }
        return dao.searchPartiesByName(keyword);
    }

    // 3. 거래처 삭제 로직 (Soft Delete 구현)
    public void deleteParty(String partyId) throws SQLException { // ★ Soft Delete 메서드 추가
        if (partyId == null || partyId.trim().isEmpty()) {
            throw new IllegalArgumentException("거래처 ID는 필수입니다.");
        }

        // DAO의 softDeleteById 호출
        int result = dao.softDeleteById(partyId);

        if (result == 0) {
            throw new IllegalArgumentException("거래처 삭제 실패: 해당 ID를 찾을 수 없습니다.");
        }
    }

    // 4. 거래처 원장 상세 내역 조회 (핵심 미션 구현)
    public List<PartyLedgerDTO> getLedgerLinesByPartyId(String partyId) throws SQLException { // ★ throws SQLException 추가
        if (partyId == null || partyId.trim().isEmpty()) {
            throw new IllegalArgumentException("거래처 ID는 필수입니다.");
        }
        return dao.selectLedgerLinesByPartyId(partyId);
    }

}