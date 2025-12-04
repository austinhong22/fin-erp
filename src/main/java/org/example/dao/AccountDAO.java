package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.AccountDTO;
import org.example.dto.AccountType; // 계정과목의 ENUM 타입

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {

    // 1. INSERT (is_active='Y' 명시적 추가)
    public int insert(AccountDTO dto) throws SQLException {
        // ★ is_active 컬럼을 명시적으로 'Y'로 추가합니다.
        String sql = "INSERT INTO gl_account (id, company_id, code, name, type, is_active) VALUES (?, ?, ?, ?, ?, 'Y')";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getCompanyId());
            pstmt.setString(3, dto.getCode());
            pstmt.setString(4, dto.getName());
            pstmt.setString(5, dto.getType().name()); // ENUM을 String으로 변환

            return pstmt.executeUpdate();
        }
    }

    // ============================================
    // ★ 2. Soft Delete 구현 (DELETE 대신 UPDATE)
    // ============================================
    public int softDeleteById(String id) throws SQLException {
        // 실제 DELETE 대신 is_active 값을 'N'(비활성)으로 변경합니다.
        String sql = "UPDATE gl_account SET is_active = 'N' WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            return pstmt.executeUpdate();
        }
    }

    // ============================================
    // ★ 3. 전체 조회 (활성 필터링)
    // ============================================
    public List<AccountDTO> findAll() throws SQLException {
        // ★ WHERE is_active = 'Y' 조건 추가
        String sql = "SELECT * FROM gl_account WHERE is_active = 'Y'";
        List<AccountDTO> accountInfo = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                AccountDTO dto = new AccountDTO();
                dto.setId(rs.getString("id"));
                dto.setCompanyId(rs.getString("company_id"));
                dto.setCode(rs.getString("code"));
                dto.setName(rs.getString("name"));
                dto.setType(AccountType.valueOf(rs.getString("type"))); // String을 ENUM으로 변환

                accountInfo.add(dto);
            }
            return accountInfo;
        }
    }

    // ============================================
    // ★ 4. ID로 단일 조회 (활성 필터링)
    // ============================================
    public AccountDTO selectById(String id) throws SQLException {
        // ★ WHERE is_active = 'Y' 조건 추가
        String sql = "SELECT * FROM gl_account WHERE id = ? AND is_active = 'Y'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    AccountDTO dto = new AccountDTO();
                    dto.setId(rs.getString("id"));
                    dto.setCompanyId(rs.getString("company_id"));
                    dto.setCode(rs.getString("code"));
                    dto.setName(rs.getString("name"));
                    dto.setType(AccountType.valueOf(rs.getString("type")));
                    return dto;
                }
            }
        }
        return null;
    }
}