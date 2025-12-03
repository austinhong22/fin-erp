package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.JournalEntryDTO;
import org.example.dto.JournalLineDTO;
import org.example.dto.PartyDTO;
import org.example.dto.PartyLedgerDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartyDAO {

    // 거래처 등록 (C)
    public int insert(PartyDTO dto) throws SQLException {
        // company_id와 registration_number 필드 추가
        String sql = "INSERT INTO party (id, company_id, name, type, contact, registration_number) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getCompanyId());
            pstmt.setString(3, dto.getName());
            pstmt.setString(4, dto.getType());
            pstmt.setString(5, dto.getContact());
            pstmt.setString(6, dto.getRegistrationNumber());

            return pstmt.executeUpdate();
        }
    }

    //  타입별 거래처 조회 (R)
    public List<PartyDTO> selectByType(String type) throws SQLException {
        String sql = "SELECT id, company_id, name, type, contact, registration_number FROM party WHERE type = ?";
        List<PartyDTO> partyList = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type); // 타입(CUSTOMER 또는 VENDOR) 바인딩

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PartyDTO dto = new PartyDTO();
                    dto.setId(rs.getString("id"));
                    dto.setCompanyId(rs.getString("company_id"));
                    dto.setName(rs.getString("name"));
                    dto.setType(rs.getString("type"));
                    dto.setContact(rs.getString("contact"));
                    dto.setRegistrationNumber(rs.getString("registration_number"));

                    partyList.add(dto);
                }
            }
        }
        return partyList;
    }
    // 거래처 전체 목록 조회
    public List<PartyDTO> selectAll() throws SQLException {
        String sql = "SELECT id, company_id, name, type, contact, registration_number FROM party";
        List<PartyDTO> partyList = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                PartyDTO dto = new PartyDTO();
                dto.setId(rs.getString("id"));
                dto.setCompanyId(rs.getString("company_id"));
                dto.setName(rs.getString("name"));
                dto.setType(rs.getString("type"));
                dto.setContact(rs.getString("contact"));
                dto.setRegistrationNumber(rs.getString("registration_number"));

                partyList.add(dto);
            }
        }
        return partyList;
    }

    // 거래처 이름 키워드 검색
    public List<PartyDTO> searchPartiesByName(String nameKeyword) throws SQLException {
        String sql = "SELECT id, company_id, name, type, contact, registration_number FROM party WHERE name LIKE ?";
        List<PartyDTO> partyList = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, "%" + nameKeyword + "%");

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PartyDTO dto = new PartyDTO();
                    dto.setId(rs.getString("id"));
                    dto.setCompanyId(rs.getString("company_id"));
                    dto.setName(rs.getString("name"));
                    dto.setType(rs.getString("type"));
                    dto.setContact(rs.getString("contact"));
                    dto.setRegistrationNumber(rs.getString("registration_number"));

                    partyList.add(dto);
                }
            }
        }
        return partyList;
    }

    // 거래처 원장 상세 내역 조회
    // 반환 타입을 List<PartyLedgerDTO>로 변경
    public List<PartyLedgerDTO> selectLedgerLinesByPartyId(String partyId) throws SQLException {

        // je(전표 헤더), jl(분개 라인), gl(계정 과목) 조인
        // 쿼리 결과에 entry_date, description(적요), gl_account_name 포함
        String sql = "SELECT " +
                "  je.entry_date, " +
                "  je.description, " + // 전표 헤더 적요를 사용
                "  gl.name AS gl_account_name, " + // 계정 과목 이름
                "  jl.debit_amount, " +
                "  jl.credit_amount " +
                "FROM journal_entry je " +
                "JOIN journal_line jl ON je.id = jl.journal_entry_id " +
                "JOIN gl_account gl ON jl.gl_account_id = gl.id " +
                "WHERE je.party_id = ? " + // 해당 거래처(party_id)로 필터링
                "ORDER BY je.entry_date DESC";

        // 💡 반환 타입에 맞게 리스트 생성
        List<PartyLedgerDTO> ledgerList = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, partyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // PartyLedgerDTO 객체 생성
                    PartyLedgerDTO dto = new PartyLedgerDTO();

                    // 명확한 필드 매핑
                    dto.setEntryDate(rs.getString("entry_date"));
                    dto.setDescription(rs.getString("description")); // 적요
                    dto.setGlAccountName(rs.getString("gl_account_name")); // 계정 과목 이름

                    dto.setDebitAmount(rs.getBigDecimal("debit_amount"));
                    dto.setCreditAmount(rs.getBigDecimal("credit_amount"));

                    ledgerList.add(dto);
                }
            }
        }
        return ledgerList; // PartyLedgerDTO 리스트 반환
    }
}


