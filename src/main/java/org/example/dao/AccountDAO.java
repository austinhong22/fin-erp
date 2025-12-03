package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.AccountDTO;
import org.example.dto.AccountType;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AccountDAO {


    public int insert(AccountDTO dto) {
        String sql = "INSERT INTO gl_account (id, company_id, code, name, type) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;


        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getCompanyId());
            pstmt.setString(3, dto.getCode());
            pstmt.setString(4, dto.getName());
            pstmt.setString(5, dto.getType().name());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("insert error", e);

        } finally {
            close(conn, pstmt, null);
        }
    }


    public List<AccountDTO> findAll(){
        String sql = "SELECT * FROM gl_account";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<AccountDTO> accountInfo = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                AccountDTO dto = new AccountDTO();
                dto.setId(rs.getString("id"));
                dto.setCompanyId(rs.getString("company_id"));
                dto.setCode(rs.getString("code"));
                dto.setName(rs.getString("name"));
                dto.setType(AccountType.valueOf(rs.getString("type")));

                accountInfo.add(dto);
            }
            return accountInfo;

        } catch (SQLException e) {
            throw new IllegalStateException("query error", e);

        } finally {
            close(conn, pstmt, rs);
        }
    }


    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (pstmt != null) pstmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public AccountDTO selectById(String id) {
        String sql = "SELECT * FROM gl_account WHERE id = ?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                AccountDTO dto = new AccountDTO();
                dto.setId(rs.getString("id"));
                dto.setCompanyId(rs.getString("company_id"));
                dto.setCode(rs.getString("code"));
                dto.setName(rs.getString("name"));
                dto.setType(AccountType.valueOf(rs.getString("type")));
                return dto;
            }
            return null; // ID에 해당하는 계정이 없는 경우

        } catch (SQLException e) {
            // DAO는 데이터 접근 오류만 처리하고 Service에게 예외를 전달하는 것이 일반적입니다.
            throw new IllegalStateException("selectById error", e);

        } finally {
            // 기존 close 유틸리티 메서드를 사용하여 자원 해제
            close(conn, pstmt, rs);
        }
    }


}


