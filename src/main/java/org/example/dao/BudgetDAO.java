package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.BudgetDTO;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BudgetDAO {

    // ============================
    // 1. INSERT (예산 등록)
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
    // 2. 중복 체크 (exists)
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
                    return rs.getInt(1) > 0; // 1개 이상이면 true (중복)
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return true; // 에러 시 안전하게 중복으로 간주
        }
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

    // ============================
    // 4. UPDATE (예산 금액 수정) - ★ 추가됨
    // ============================
    public int updateAmount(String id, BigDecimal newAmount) {
        String sql = "UPDATE `budget` SET `budget_amount` = ? WHERE `id` = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setBigDecimal(1, newAmount);
            pstmt.setString(2, id);

            return pstmt.executeUpdate(); // 수정된 행 개수 반환
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // ============================
    // 5. DELETE (예산 삭제) - ★ 추가됨
    // ============================
    public int delete(String id) {
        String sql = "DELETE FROM `budget` WHERE `id` = ?";

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, id);

            return pstmt.executeUpdate(); // 삭제된 행 개수 반환
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}