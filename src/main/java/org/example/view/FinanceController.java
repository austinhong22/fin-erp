package org.example.view;

import org.example.dto.AccountType;
import org.example.dto.ReportDTO;
import org.example.service.FinanceService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class FinanceController {
    private Scanner sc;

    private FinanceService financeService = new FinanceService();

    public FinanceController(Scanner sc){ this.sc = sc;  }

    // 진입점 메서드
    public void start() {
        while (true) {
            System.out.println("\n=== 재무 및 기준 정보 관리 ===");
            System.out.println("1. 등록 기능 (계정/계좌)");
            System.out.println("2. 조회 기능 (리포트)");
            System.out.println("3. 사용 중지/삭제 (계정)"); // ★ 메뉴 추가
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 > ");

            String menu = sc.nextLine();
            if ("0".equals(menu)) break;

            // Service가 throws SQLException을 던지므로, Controller에서 최종적으로 잡습니다.
            try {
                if ("1".equals(menu)){
                    System.out.println("1. 계정과목 등록하기");
                    System.out.println("2. 은행 계좌 등록하기");

                    String sideMenu = sc.nextLine();

                    register(sideMenu); // throws SQLException 전파
                }
                else if ("2".equals(menu)) {
                    search(); // throws SQLException 전파
                }
                else if ("3".equals(menu)) { // ★ 메뉴 처리
                    deactivateMasterData(); // throws SQLException 전파
                }
            } catch (SQLException e) {
                // DB 관련 오류 발생 시 시스템 오류로 안내
                System.out.println("❌ DB 작업 중 오류가 발생했습니다: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                // Service에서 던진 입력값 유효성 검사 예외 처리
                System.out.println("❌ 입력 오류: " + e.getMessage());
            } catch (Exception e) {
                // 일반 예외 처리
                System.out.println("❌ 일반 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // 1. 등록 기능 (Account, BankAccount)
    private void register(String sideMenu) throws SQLException {
        if(sideMenu.equals("1")){
            System.out.println("\n[계정 과목 등록]");
            System.out.print("계정 과목 코드를 입력해주세요: ");
            String code = sc.nextLine();
            System.out.print("계정 과목명을 입력해주세요: ");
            String name = sc.nextLine();
            System.out.println("등록하려는 계정 타입을 선택해주세요: ");
            System.out.println("1. ASSET(자산) / 2. REVENUE(수익) / 3. EXPENSE(비용)");

            AccountType accountType = null;
            String type = sc.nextLine();

            switch (type){
                case "1" : accountType = AccountType.ASSET; break;
                case "2" : accountType = AccountType.REVENUE; break;
                case "3" : accountType = AccountType.EXPENSE; break;
                default:
                    throw new IllegalArgumentException("잘못된 계정 타입 선택 (1, 2, 3 중 하나를 선택해야 합니다).");
            }

            if (accountType == null) {
                throw new IllegalArgumentException("계정 타입 선택이 누락되었습니다.");
            }

            financeService.registerAccount(code,name,accountType);
            System.out.println("✅ 계정 과목 등록 요청 완료.");

        }
        else if(sideMenu.equals("2")){
            System.out.println("\n[은행 계좌 등록]");
            System.out.print("은행명을 입력해주세요: ");
            String bankName = sc.nextLine();

            System.out.print("은행 계좌를 입력해주세요: ");
            String accountNo = sc.nextLine();

            System.out.print("별칭을 입력해주세요: ");
            String accountAlias = sc.nextLine();

            System.out.print("기타 추가할 내용 입력: ");
            String description = sc.nextLine();

            financeService.registerBankAccount(bankName,accountNo, accountAlias, description);
            System.out.println("✅ 은행 계좌 등록 요청 완료.");
        } else {
            throw new IllegalArgumentException("잘못된 서브 메뉴 선택입니다.");
        }
    }

    // ★ 3. 사용 중지/삭제 기능 (계정과목) 구현
    private void deactivateMasterData() throws SQLException {
        System.out.println("\n[기준 정보 사용 중지 (Soft Delete)]");
        System.out.println("1. 계정과목 사용 중지");
        System.out.println("2. 은행 계좌 사용 중지 (TODO)"); // 아직 Service에 미구현된 BankAccount도 메뉴로 표시
        System.out.print("선택: ");
        String choice = sc.nextLine();

        if ("1".equals(choice)) {
            System.out.print("사용 중지할 계정 ID를 입력하세요: ");
            String accountId = sc.nextLine();

            System.out.print("정말로 이 계정과목을 비활성화(사용 중지) 하시겠습니까? (y/n): ");
            String confirm = sc.nextLine();

            if (!"y".equalsIgnoreCase(confirm)) {
                System.out.println("🚫 작업 취소.");
                return;
            }

            // Service 호출 (throws SQLException)
            financeService.deleteAccount(accountId);
            System.out.println("✅ 계정과목 ID [" + accountId + "] 사용 중지 완료.");

        } else if ("2".equals(choice)) {
            System.out.println("❌ 은행 계좌 사용 중지 기능은 현재 구현 중입니다. BankAccountDAO에 softDeleteById를 추가해야 합니다.");
        } else {
            throw new IllegalArgumentException("잘못된 선택입니다.");
        }
    }

    // 2. 조회 기능 분기
    private void search() throws SQLException {
        showBudgetReport();
    }

    /**
     * 예산 대비 실적 리포트 조회
     */
    public void showBudgetReport() throws SQLException {
        System.out.print("\n조회하려는 연월을 입력해주세요 : (예: 202501) ");
        String yearMonth = sc.nextLine();

        System.out.println("===========================================");
        System.out.println("           [예산 대비 실적 리포트]");
        System.out.println("===========================================");
        System.out.printf("조회 연월: %s%n%n", yearMonth);


        List<ReportDTO> budgetReport = financeService.showBudgetReport(yearMonth);

        if (budgetReport == null || budgetReport.isEmpty()) {
            System.out.println("조회 결과가 없습니다.");
            return;
        }

        System.out.printf("%-10s %-12s %15s %15s %15s%n",
                "부서", "계정", "예산", "지출", "잔액");
        System.out.println("---------------------------------------------------------------");


        for (ReportDTO row : budgetReport) {
            System.out.printf("%-10s %-12s %15s %15s %15s%n",
                    row.getDeptName(),
                    row.getAccountName(),
                    row.getBudgetAmount(),
                    row.getExpenseAmount(),
                    row.getBalance()
            );
        }

        System.out.println("---------------------------------------------------------------");

    }
}