package org.example.view;

import org.example.dto.JournalEntryDTO;
import org.example.dto.JournalLineDTO;
import org.example.service.JournalService;

import java.math.BigDecimal;
import java.util.Scanner;

public class JournalController {

    private Scanner sc;
    private JournalService journalService = new JournalService();

    public JournalController(Scanner sc) {
        this.sc = sc;
    }

    /**
     * 전표 입력 메뉴 진입점
     */
    public void start() {
        while (true) {
            System.out.println("\n=== 전표 입력 ===");
            System.out.println("1. 전표 등록");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 > ");

            String menu = sc.nextLine();

            if ("0".equals(menu)) {
                break;
            } else if ("1".equals(menu)) {
                registerJournal();
            } else {
                System.out.println("잘못된 입력입니다.");
            }
        }
    }

    /**
     * 전표 등록 처리
     */
    private void registerJournal() {
        try {
            JournalEntryDTO entry = new JournalEntryDTO();

            // 1. 전표 헤더 정보 입력
            System.out.println("\n--- 전표 헤더 정보 입력 ---");
            System.out.print("전표일자 (YYYY-MM-DD): ");
            entry.setEntryDate(sc.nextLine());

            System.out.print("설명입력 : ");
            entry.setDescription(sc.nextLine());

            System.out.print("거래처 ID (선택사항, Enter로 건너뛰기): ");
            String partyId = sc.nextLine();
            if (!partyId.trim().isEmpty()) {
                entry.setPartyId(partyId);
            }

            System.out.print("은행계좌 ID (선택사항, Enter로 건너뛰기): ");
            String bankAccountId = sc.nextLine();
            if (!bankAccountId.trim().isEmpty()) {
                entry.setBankAccountId(bankAccountId);
            }

            // 2. 전표 라인 정보 입력
            System.out.println("\n--- 전표 라인 정보 입력 ---");
            System.out.print("라인 개수: ");
            int lineCount = Integer.parseInt(sc.nextLine());

            for (int i = 1; i <= lineCount; i++) {
                System.out.println("\n[라인 " + i + "]");
                JournalLineDTO line = new JournalLineDTO();

                System.out.print("계정과목 ID: ");
                line.setGlAccountId(sc.nextLine());

                System.out.print("부서 ID (선택사항, Enter로 건너뛰기): ");
                String deptId = sc.nextLine();
                if (!deptId.trim().isEmpty()) {
                    line.setDepartmentId(deptId);
                }

                System.out.print("차변 금액 (0 입력 시 대변): ");
                BigDecimal debitAmount = new BigDecimal(sc.nextLine());
                line.setDebitAmount(debitAmount);

                if (debitAmount.compareTo(BigDecimal.ZERO) == 0) {
                    System.out.print("대변 금액: ");
                    BigDecimal creditAmount = new BigDecimal(sc.nextLine());
                    line.setCreditAmount(creditAmount);
                } else {
                    line.setCreditAmount(BigDecimal.ZERO);
                }

                System.out.print("메모 (선택사항, Enter로 건너뛰기): ");
                String memo = sc.nextLine();
                if (!memo.trim().isEmpty()) {
                    line.setMemo(memo);
                }

                entry.addLine(line);
            }

            // 3. Service 호출하여 저장
            journalService.registerJournal(entry);
            System.out.println("\n✅ 전표 등록이 완료되었습니다.");

        } catch (NumberFormatException e) {
            System.out.println("❌ 숫자 형식이 올바르지 않습니다: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 검증 실패: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("❌ 오류 발생: " + e.getMessage());
            e.printStackTrace();
        }
    }
}






