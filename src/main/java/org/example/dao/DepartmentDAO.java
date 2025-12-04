package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.DepartmentDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// 부서 테이블 DAO (PartyDAO와 구조 통일)
public class DepartmentDAO {

    // ====================================================
    // 1. INSERT (Soft Delete 적용: is_active = 'Y' 기본값)
    // ====================================================
    public int insert(DepartmentDTO dto) throws SQLException {

        String sql = "INSERT INTO department (id, company_id, name, code, is_active) VALUES (?, ?, ?, ?, 'Y')";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getCompanyId());
            pstmt.setString(3, dto.getName());
            pstmt.setString(4, dto.getCode());

            return pstmt.executeUpdate();
        }
    }

    // ====================================================
    // 2. UPDATE (name, code 수정)
    // ====================================================
    public int update(DepartmentDTO dto) throws SQLException {

        String sql = "UPDATE department SET name = ?, code = ? WHERE id = ? AND is_active = 'Y'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getCode());
            pstmt.setString(3, dto.getId());

            return pstmt.executeUpdate();
        }
    }

    // ====================================================
    // 3. SOFT DELETE (is_active = 'N') - 사용 중지
    // ====================================================
    public int softDeleteById(String id) throws SQLException {

        String sql = "UPDATE department SET is_active = 'N' WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            return pstmt.executeUpdate();
        }
    }

    // ====================================================
    // ★ 4. RESTORE (is_active = 'Y') - 복구 기능 추가
    // ====================================================
    public int restoreById(String id) throws SQLException {

        String sql = "UPDATE department SET is_active = 'Y' WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            return pstmt.executeUpdate();
        }
    }

    // ====================================================
    // 5. SELECT BY ID (활성 부서만)
    // ====================================================
    public DepartmentDTO selectById(String id) throws SQLException {

        String sql = "SELECT id, company_id, name, code " +
                "FROM department WHERE id = ? AND is_active = 'Y'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new DepartmentDTO(
                            rs.getString("id"),
                            rs.getString("company_id"),
                            rs.getString("name"),
                            rs.getString("code")
                    );
                }
            }
        }
        return null;
    }

    // ====================================================
    // 6. SELECT BY COMPANY ID (활성 부서 목록)
    // ====================================================
    public List<DepartmentDTO> selectByCompanyId(String companyId) throws SQLException {

        String sql = "SELECT id, company_id, name, code " +
                "FROM department WHERE company_id = ? AND is_active = 'Y' ORDER BY name";

        List<DepartmentDTO> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, companyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new DepartmentDTO(
                            rs.getString("id"),
                            rs.getString("company_id"),
                            rs.getString("name"),
                            rs.getString("code")
                    ));
                }
            }
        }
        return list;
    }

    // ====================================================
    // ★ 7. SELECT DEACTIVATED BY COMPANY ID (비활성 부서 목록 추가)
    // ====================================================
    public List<DepartmentDTO> selectDeactivatedByCompanyId(String companyId) throws SQLException {

        String sql = "SELECT id, company_id, name, code " +
                "FROM department WHERE company_id = ? AND is_active = 'N' ORDER BY name";

        List<DepartmentDTO> list = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, companyId);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    list.add(new DepartmentDTO(
                            rs.getString("id"),
                            rs.getString("company_id"),
                            rs.getString("name"),
                            rs.getString("code")
                    ));
                }
            }
        }
        return list;
    }

    // ====================================================
    // 8. 중복 체크: CODE (활성 부서만)
    // ====================================================
    public DepartmentDTO selectByCode(String code) throws SQLException {

        String sql = "SELECT id, company_id, name, code " +
                "FROM department WHERE code = ? AND is_active = 'Y'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, code);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return new DepartmentDTO(
                            rs.getString("id"),
                            rs.getString("company_id"),
                            rs.getString("name"),
                            rs.getString("code")
                    );
                }
            }
        }
        return null;
    }

    // ====================================================
    // 9. 중복 체크: NAME (활성 부서만)
    // ====================================================
    public DepartmentDTO selectByName(String name) throws SQLException {

        String sql = "SELECT id, company_id, name, code " +
                "FROM department WHERE name = ? AND is_active = 'Y'";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, name);

            try (ResultSet rs = pstmt.executeQuery()) {

                if (rs.next()) {
                    return new DepartmentDTO(
                            rs.getString("id"),
                            rs.getString("company_id"),
                            rs.getString("name"),
                            rs.getString("code")
                    );
                }
            }
        }
        return null;
    }
}