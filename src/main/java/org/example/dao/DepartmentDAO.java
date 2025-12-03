package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.DepartmentDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// 부서 테이블 DAO
public class DepartmentDAO {

    // INSERT
    public int insert(DepartmentDTO dto) {
        String sql = "INSERT INTO department (id, company_id, name, code) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getCompanyId());
            pstmt.setString(3, dto.getName());
            pstmt.setString(4, dto.getCode());

            return pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // UPDATE (name, code 수정)
    public int update(DepartmentDTO dto) {
        String sql = "UPDATE department SET name = ?, code = ? WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getName());
            pstmt.setString(2, dto.getCode());
            pstmt.setString(3, dto.getId());

            return pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ID로 단건 조회 (순서 수정된 올바른 버전)
    public DepartmentDTO selectById(String id) {
        String sql = "SELECT * FROM department WHERE id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return new DepartmentDTO(
                        rs.getString("id"),
                        rs.getString("company_id"),
                        rs.getString("name"),
                        rs.getString("code")
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // 특정 회사의 부서 목록 조회
    public List<DepartmentDTO> selectByCompanyId(String companyId) {
        List<DepartmentDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM department WHERE company_id = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, companyId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                list.add(new DepartmentDTO(
                        rs.getString("id"),
                        rs.getString("company_id"),
                        rs.getString("name"),
                        rs.getString("code")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
}
