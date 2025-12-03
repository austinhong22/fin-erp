package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.PartyDTO;

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
}
