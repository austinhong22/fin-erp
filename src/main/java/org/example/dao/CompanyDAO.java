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
    public int insert(CompanyDTO dto) {
        String sql = "INSERT INTO company (id, name, business_no, created_at) VALUES (?, ?, ?, NOW())";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getName());
            pstmt.setString(3, dto.getBusinessNo());

            return pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // UPDATE (회사명, 사업자번호 수정)
    public int update(CompanyDTO dto) {
        String sql = "UPDATE company SET name = ?, business_no = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getBusinessNo());
            pstmt.setString(3, dto.getId());

            return pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // 전체 조회
    public List<CompanyDTO> selectAll() {
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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ID로 조회 (순서 수정된 버전)
    public CompanyDTO selectById(String id) {
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

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
