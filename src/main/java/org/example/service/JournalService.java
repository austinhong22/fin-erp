package org.example.service;

import org.example.config.DBUtil;
import org.example.dao.JournalDAO;
import org.example.dto.JournalEntryDTO;
import org.example.dto.JournalLineDTO;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public class JournalService {

    private final JournalDAO journalDAO = new JournalDAO();


    public void registerJournal(JournalEntryDTO entry) throws SQLException {
        Connection conn = null;

        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            // 1. 차변/대변 합계 검증
            validateDebitCreditBalance(entry.getLines());

            // 2. 전표 헤더 INSERT
            if (entry.getId() == null || entry.getId().isEmpty()) {
                entry.setId(UUID.randomUUID().toString());
            }
            int entryResult = journalDAO.insertEntry(conn, entry);
            if (entryResult <= 0) {
                throw new SQLException("전표 헤더 저장 실패");
            }

            // 3. 전표 라인들 INSERT
            List<JournalLineDTO> lines = entry.getLines();
            for (JournalLineDTO line : lines) {
                if (line.getId() == null || line.getId().isEmpty()) {
                    line.setId(UUID.randomUUID().toString());
                }
                line.setJournalEntryId(entry.getId()); // 헤더 ID 연결

                int lineResult = journalDAO.insertLine(conn, line);
                if (lineResult <= 0) {
                    throw new SQLException("전표 라인 저장 실패: " + line);
                }
            }

            // 4. 모든 작업 성공 시 커밋
            conn.commit();
            System.out.println("✅ 전표 등록 완료: " + entry.getId());

        } catch (Exception e) {
            // 5. 오류 발생 시 롤백
            if (conn != null) {
                conn.rollback();
                System.out.println("❌ 전표 등록 실패: 롤백 처리됨");
            }
            throw e;
        } finally {
            // 6. Connection 정리
            if (conn != null) {
                conn.setAutoCommit(true); // 원래대로 복구
                conn.close();
            }
        }
    }

    /**
     * 차변 합계와 대변 합계가 같은지 검증 (복식부기 원칙)
     * 
     * @param lines 전표 라인 목록
     * @throws IllegalArgumentException 차변/대변 불일치 시
     */
    private void validateDebitCreditBalance(List<JournalLineDTO> lines) {
        BigDecimal debitSum = BigDecimal.ZERO;
        BigDecimal creditSum = BigDecimal.ZERO;

        for (JournalLineDTO line : lines) {
            if (line.getDebitAmount() != null) {
                debitSum = debitSum.add(line.getDebitAmount());
            }
            if (line.getCreditAmount() != null) {
                creditSum = creditSum.add(line.getCreditAmount());
            }
        }

        if (debitSum.compareTo(creditSum) != 0) {
            throw new IllegalArgumentException(
                    String.format("차변/대변 불일치: 차변 합계=%s, 대변 합계=%s", debitSum, creditSum));
        }
    }
}
