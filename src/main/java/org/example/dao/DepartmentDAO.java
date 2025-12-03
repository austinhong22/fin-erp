package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.DepartmentDTO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// 부서 테이블 DAO
public class DepartmentDAO {

    // 부서 INSERT
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
