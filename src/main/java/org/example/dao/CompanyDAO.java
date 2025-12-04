package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.CompanyDTO;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

// 회사 테이블을 다루는 DAO
public class CompanyDAO {

    // INSERT
    public int insert(CompanyDTO dto) throws SQLException { // ★ throws SQLException 추가
        String sql = "INSERT INTO company (id, name, business_no, created_at) VALUES (?, ?, ?, NOW())";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getName());
            pstmt.setString(3, dto.getBusinessNo());

            return pstmt.executeUpdate();
        }
        // catch 블록 제거: Service가 오류를 처리하도록 위임
    }

    // UPDATE (회사명, 사업자번호 수정)
    public int update(CompanyDTO dto) throws SQLException { // ★ throws SQLException 추가
        String sql = "UPDATE company SET name = ?, business_no = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getBusinessNo());
            pstmt.setString(3, dto.getId());

            return pstmt.executeUpdate();
        }
        // catch 블록 제거
    }

    // 전체 조회
    public List<CompanyDTO> selectAll() throws SQLException { // ★ throws SQLException 추가
        List<CompanyDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM company";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = (ts != null ? ts.toLocalDateTime() : null);

                list.add(new CompanyDTO(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("business_no"),
                        createdAt
                ));
            }
        }
        return list;
        // catch 블록 제거
    }

    // ID로 조회
    public CompanyDTO selectById(String id) throws SQLException { // ★ throws SQLException 추가
        String sql = "SELECT * FROM company WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                Timestamp ts = rs.getTimestamp("created_at");
                LocalDateTime createdAt = (ts != null ? ts.toLocalDateTime() : null);

                return new CompanyDTO(
                        rs.getString("id"),
                        rs.getString("name"),
                        rs.getString("business_no"),
                        createdAt
                );
            }
        }
        return null;
        // catch 블록 제거
    }
}