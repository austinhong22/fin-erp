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
        // is_active 필드를 추가하지 않아도 DB에서 DEFAULT 'Y'로 저장됨
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

    // 1. DELETE 기능  (Soft Delete 적용)
    // 실제 삭제 대신 is_active를 'N'으로 변경
    public int softDeleteById(String id) throws SQLException {
        // DTO에 is_active 필드가 없더라도, DB에 직접 UPDATE 명령을 내릴 수 있습니다.
        String sql = "UPDATE party SET is_active = 'N' WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            return pstmt.executeUpdate();
        }
    }


    // 2. ID로 단일 거래처 조회 기능 추가 (JournalService 유효성 검사용)
    public PartyDTO selectById(String id) throws SQLException {
        // is_active='Y'인 활성 상태의 거래처만 조회
        String sql = "SELECT id, company_id, name, type, contact, registration_number FROM party WHERE id = ? AND is_active = 'Y'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    PartyDTO dto = new PartyDTO();
                    dto.setId(rs.getString("id"));
                    dto.setCompanyId(rs.getString("company_id"));
                    dto.setName(rs.getString("name"));
                    dto.setType(rs.getString("type"));
                    dto.setContact(rs.getString("contact"));
                    dto.setRegistrationNumber(rs.getString("registration_number"));
                    return dto;
                }
            }
        }
        return null; // ID를 찾지 못했거나 비활성화된 경우
    }
    // 3. 기존 조회 기능 수정: is_active = 'Y' 필터

    // 타입별 거래처 조회 (R)
    public List<PartyDTO> selectByType(String type) throws SQLException {
        // ★ is_active = 'Y' 필터 추가
        String sql = "SELECT id, company_id, name, type, contact, registration_number FROM party WHERE type = ? AND is_active = 'Y'";
        List<PartyDTO> partyList = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, type);

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
        // ★ is_active = 'Y' 필터 추가
        String sql = "SELECT id, company_id, name, type, contact, registration_number FROM party WHERE is_active = 'Y'";
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
        // ★ is_active = 'Y' 필터 추가
        String sql = "SELECT id, company_id, name, type, contact, registration_number FROM party WHERE name LIKE ? AND is_active = 'Y'";
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

    // 거래처 원장 상세 내역 조회 (리포트용)
    public List<PartyLedgerDTO> selectLedgerLinesByPartyId(String partyId) throws SQLException {

        // 이 쿼리는 Journal Entry를 참조하므로 is_active를 추가하지 않습니다.
        // 과거 거래 내역은 비활성화된 거래처라도 보여줘야 합니다.
        String sql = "SELECT " +
                "  je.entry_date, " +
                "  je.description, " +
                "  gl.name AS gl_account_name, " +
                "  jl.debit_amount, " +
                "  jl.credit_amount, " +
                "  registration_number " +
                "FROM journal_entry je " +
                "JOIN journal_line jl ON je.id = jl.journal_entry_id " +
                "JOIN gl_account gl ON jl.gl_account_id = gl.id " +
                "JOIN party p ON je.party_id = p.id " +
                "WHERE je.party_id = ? " +
                "ORDER BY je.entry_date";

        List<PartyLedgerDTO> ledgerList = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, partyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    PartyLedgerDTO dto = new PartyLedgerDTO();
                    dto.setEntryDate(rs.getString("entry_date"));
                    dto.setDescription(rs.getString("description"));
                    dto.setGlAccountName(rs.getString("gl_account_name"));
                    dto.setRegistration(rs.getString("registration_number"));
                    dto.setDebitAmount(rs.getBigDecimal("debit_amount"));
                    dto.setCreditAmount(rs.getBigDecimal("credit_amount"));

                    ledgerList.add(dto);
                }
            }
        }
        return ledgerList;
    }
}