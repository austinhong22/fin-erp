package org.example.dao;

import org.example.dto.JournalEntryDTO;
import org.example.dto.JournalLineDTO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class JournalDAO {


    public int insertEntry(Connection conn, JournalEntryDTO entry) throws SQLException {
        String sql = "INSERT INTO journal_entry (id, company_id, entry_date, description, party_id, bank_account_id) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, entry.getId());
            pstmt.setString(2, "CP-001"); // 고정 회사 ID
            pstmt.setString(3, entry.getEntryDate());
            pstmt.setString(4, entry.getDescription());
            pstmt.setString(5, entry.getPartyId());
            pstmt.setString(6, entry.getBankAccountId());

            return pstmt.executeUpdate();
        }
    }


    public int insertLine(Connection conn, JournalLineDTO line) throws SQLException {
        String sql = "INSERT INTO journal_line (id, journal_entry_id, gl_account_id, department_id, debit_amount, credit_amount, memo) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, line.getId());
            pstmt.setString(2, line.getJournalEntryId());
            pstmt.setString(3, line.getGlAccountId());
            pstmt.setString(4, line.getDepartmentId());
            pstmt.setBigDecimal(5, line.getDebitAmount());
            pstmt.setBigDecimal(6, line.getCreditAmount());
            pstmt.setString(7, line.getMemo());

            return pstmt.executeUpdate();
        }
    }
}





