package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.DepartmentDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DepartmentDAO {

    // 부서 등록
    public int insert(DepartmentDTO dto){
        String sql = "INSERT INTO department (id, company_id, name, code) VALUES (?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getCompanyId());
            pstmt.setString(3, dto.getName());
            pstmt.setString(4, dto.getCode());

            return pstmt.executeUpdate(); // 성공여부 반환

        } catch (SQLException e){
            e.printStackTrace();
        }
        return -1;
    }
    // 부서 전체 조회
    public List<DepartmentDTO> selectAll() throws SQLException {
        String sql = "SELECT id, company_id, name, code FROM department";
        List<DepartmentDTO> deptList = new ArrayList<>();

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                DepartmentDTO dto = new DepartmentDTO();
                dto.setId(rs.getString("id"));
                dto.setCompanyId(rs.getString("company_id"));
                dto.setName(rs.getString("name"));
                dto.setCode(rs.getString("code"));

                deptList.add(dto);
            }
        }
        return deptList;
    }
}
