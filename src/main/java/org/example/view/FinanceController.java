package org.example.view;

import org.example.dto.AccountType;
import org.example.dto.ReportDTO;
import org.example.service.FinanceService;

import java.util.List;
import java.util.Scanner;

public class FinanceController {
    private Scanner sc;

    private FinanceService financeService = new FinanceService();

    public FinanceController(Scanner sc){ this.sc = sc;  }

    // 진입점 메서드
    public void start() {
        while (true) {
            System.out.println("\n=== [메뉴 제목] ===");
            System.out.println("1. 등록 기능");
            System.out.println("2. 조회 기능");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 > ");

            String menu = sc.nextLine();
            if ("0".equals(menu)) break; // 루프 종료 시 Main으로 복귀

            try {
                if ("1".equals(menu)){
                    System.out.println("1. 계정과목 등록하기");
                    System.out.println("2. 은행 계좌 등록하기");

                    String sideMenu = sc.nextLine();

                    register(sideMenu);
                }
                else if ("2".equals(menu)) search();
            } catch (Exception e) {
                System.out.println("오류 발생: " + e.getMessage());
            }
        }
    }

    private void register(String sideMenu) {
        if(sideMenu.equals("1")){
            System.out.println("계정 과목 코드를 입력해주세요 : ");
            String code = sc.nextLine();
            System.out.println("계정 과목명을 입력해주세요 : ");
            String name = sc.nextLine();
            System.out.println("등록하려는 계정 타입을 선택해주세요 : ");
            System.out.println("1. ASSET(자산)" + "\n" + "2. REVENUE(수익)" +
                                "\n" + "3. EXPENSE(비용)");

            AccountType accountType = null;

            String type = sc.nextLine();
            switch (type){
                case "1" :
                    accountType = AccountType.ASSET;//자산
                    break;
                case "2" :
                    accountType = AccountType.REVENUE;//수익
                    break;
                case "3" :
                    accountType = AccountType.EXPENSE;//비용
            }

            financeService.registerAccount(code,name,accountType);
        }
        else if(sideMenu.equals("2")){
            System.out.println("은행명을 입력해주세요 : ");
            String bankName = sc.nextLine();

            System.out.println("은행 계좌를 입력해주세요 : ");
            String accountNo = sc.nextLine();

            System.out.println("별칭을 입력해주세요 : ");
            String accountAlias = sc.nextLine();

            System.out.println("기타 추가할 내용 입력 : ");
            String description = sc.nextLine();

            financeService.registerBankAccount(bankName,accountNo, accountAlias, description);
        }




    }

    private void search() {
        showBudgetReport();
    }

    /**
     * 예산 대비 실적 리포트 조회 (public 메서드로 Main에서 직접 호출 가능)
     */
    public void showBudgetReport() {
        System.out.println("조회하려는 연월을 입력해주세요 : (예: 202501) ");
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
