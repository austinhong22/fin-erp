package org.example.dao;

import org.example.config.DBUtil;
import org.example.dto.ReportDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//
public class ReportDAO {
    public List<ReportDTO> selectBudgetReport(String yearMonth) {
        List<ReportDTO> report = new ArrayList<>();

        String sql = """
                SELECT
                    d.name  AS dept,
                    ga.name AS account,
                    b.budget_amount AS budget,
                    COALESCE(SUM(jl.debit_amount), 0) AS expense,
                    (b.budget_amount - COALESCE(SUM(jl.debit_amount), 0)) AS balance
                FROM budget b
                JOIN department d ON b.department_id = d.id
                JOIN gl_account ga ON b.gl_account_id = ga.id
                LEFT JOIN journal_line jl
                    ON jl.department_id = b.department_id
                   AND jl.gl_account_id = b.gl_account_id
                LEFT JOIN journal_entry je
                    ON je.id = jl.journal_entry_id
                   AND DATE_FORMAT(STR_TO_DATE(je.entry_date, '%Y-%m-%d'), '%Y%m') = ?
                WHERE b.year_month = ?
                GROUP BY d.name, ga.name, b.budget_amount
                ORDER BY d.name, ga.name
                """;


        try (Connection conn = DBUtil.getConnection();
             PreparedStatement psmt = conn.prepareStatement(sql)) {

            psmt.setString(1, yearMonth);
            psmt.setString(2, yearMonth);

            try(ResultSet rs = psmt.executeQuery()){
                while(rs.next()){
                    report.add(new ReportDTO(
                            rs.getString("dept"),
                            rs.getString("account"),
                            rs.getBigDecimal("budget"),
                            rs.getBigDecimal("expense"),
                            rs.getBigDecimal("balance")
                    ));
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return report;
    }
}

