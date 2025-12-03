package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.BudgetDTO;

import java.math.BigDecimal; // ★ BigDecimal import
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {

    // ============================
    // 1. INSERT
    // ============================
    public int insert(BudgetDTO dto) {
        String sql = "INSERT INTO `budget` (`id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount`) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, dto.getId());
            pstmt.setString(2, dto.getCompanyId());
            pstmt.setString(3, dto.getDepartmentId());
            pstmt.setString(4, dto.getGlAccountId());
            pstmt.setString(5, dto.getYearMonth());
            pstmt.setBigDecimal(6, dto.getBudgetAmount());

            return pstmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ============================
    // 2. 중복 체크
    // ============================
    public boolean exists(String departmentId, String glAccountId, String yearMonth) {
        String sql = "SELECT COUNT(*) FROM `budget` WHERE `department_id` = ? AND `gl_account_id` = ? AND `year_month` = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, departmentId);
            pstmt.setString(2, glAccountId);
            pstmt.setString(3, yearMonth);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0; // COUNT 결과가 1 이상이면 true (중복)
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            // DB 연결 오류 시, 안전을 위해 중복으로 간주하고 true 반환
            return true;
        }

        // 결과가 0인 경우 (중복 없음)
        return false;
    }

    // ============================
    // 3. 회사 ID 기준 예산 전체 조회
    // ============================
    public List<BudgetDTO> selectByCompanyId(String companyId) {
        List<BudgetDTO> list = new ArrayList<>();
        String sql = "SELECT `id`, `company_id`, `department_id`, `gl_account_id`, `year_month`, `budget_amount` FROM `budget` WHERE `company_id` = ? ORDER BY `year_month` DESC";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, companyId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                BudgetDTO dto = new BudgetDTO(
                        rs.getString("id"),
                        rs.getString("company_id"),
                        rs.getString("department_id"),
                        rs.getString("gl_account_id"),
                        rs.getString("year_month"),
                        rs.getBigDecimal("budget_amount")
                );
                list.add(dto);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    // 이 외의 다른 메서드들은 생략했습니다.
}