package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.BankAccountDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class BankAccountDAO {
    public int insert(BankAccountDTO dto) {
        String sql = "INSERT INTO bank_account (id, company_id, bank_name, account_no, description) VALUES (?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getCompanyId());
            pstmt.setString(3, dto.getBankName());
            pstmt.setString(4, dto.getAccountNo());
            pstmt.setString(5, dto.getDescription());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            throw new IllegalStateException("은행 계좌 등록에 실패했습니다.", e);

        } finally {
            close(conn, pstmt, null);
        }
    }


    // SELECT ALL
    public List<BankAccountDTO> findAll() {
        String sql = "SELECT id, company_id, bank_name, account_no, account_alias, description FROM bank_account";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<BankAccountDTO> bankInfo = new ArrayList<>();

        try {
            conn = DBUtil.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                BankAccountDTO dto = new BankAccountDTO();
                dto.setId(rs.getString("id"));
                dto.setCompanyId(rs.getString("company_id"));
                dto.setBankName(rs.getString("bank_name"));
                dto.setAccountNo(rs.getString("account_no"));
                dto.setAccountAlias(rs.getString("account_alias"));
                dto.setDescription(rs.getString("description"));

                bankInfo.add(dto);
            }
            return bankInfo;

        } catch (SQLException e) {
            throw new IllegalStateException("은행 계좌 목록 조회에 실패했습니다.", e);

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


}
