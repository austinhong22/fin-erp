package org.example.view;

import org.example.dto.AccountDTO;
import org.example.dto.AccountType;
import org.example.dto.BankAccountDTO;
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
            System.out.println("2. 조회 기능 (계정/계좌 목록)"); // 메뉴명 수정
            System.out.println("3. 계정 관리 (삭제/복구/목록)");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 > ");

            String menu = sc.nextLine();
            if ("0".equals(menu)) break;

            // Service가 throws SQLException을 던지므로, Controller에서 최종적으로 잡습니다.
            try {
                if ("1".equals(menu)){
                    System.out.println("1. 계정 등록하기");
                    System.out.println("2. 은행 계좌 등록하기");
                    System.out.print("> 선택 : "); // System.out.println -> System.out.print
                    String sideMenu = sc.nextLine();
                    register(sideMenu);
                }
                else if ("2".equals(menu)) {
                    search();
                }
                else if ("3".equals(menu)) {
                    manageAccountMasterData();
                }
            } catch (SQLException e) {
                System.out.println("❌ DB 작업 중 오류가 발생했습니다: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                System.out.println("❌ 입력 오류: " + e.getMessage());
            } catch (Exception e) {
                System.out.println("❌ 일반 오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    // 1. 등록 기능 (Account, BankAccount)
    private void register(String sideMenu) throws SQLException {
        if(sideMenu.equals("1")){
            System.out.println("\n[계정 등록]");
            System.out.print("계정 코드를 입력해주세요: ");
            String code = sc.nextLine();
            System.out.print("계정 명을 입력해주세요: ");
            String name = sc.nextLine();
            System.out.println("등록하려는 계정 타입을 선택해주세요: ");
            System.out.println("1. ASSET(자산) / 2. REVENUE(수익) / 3. EXPENSE(비용)");

            AccountType accountType = null;
            String type = sc.nextLine();

            switch (type) {
                case "1":
                    accountType = AccountType.ASSET;
                    break;
                case "2":
                    accountType = AccountType.REVENUE;
                    break;
                case "3":
                    accountType = AccountType.EXPENSE;
                    break;
                default:
                    throw new IllegalArgumentException("잘못된 계정 타입 선택 (1, 2, 3 중 하나를 선택해야 합니다).");
            }

            if (accountType == null) {
                throw new IllegalArgumentException("계정 타입 선택이 누락되었습니다.");
            }
            financeService.registerAccount(code,name,accountType);
            System.out.println("✅ 계정 등록 요청 완료.");

        } else if (sideMenu.equals("2")) {
            System.out.println("\n[은행 계좌 등록]");
            System.out.print("은행명을 입력해주세요: ");
            String bankName = sc.nextLine();

            System.out.print("은행 계좌를 입력해주세요: ");
            String accountNo = sc.nextLine();

            System.out.print("별칭을 입력해주세요: ");
            String accountAlias = sc.nextLine();

            System.out.print("기타 추가할 내용 입력: ");
            String description = sc.nextLine();

            financeService.registerBankAccount(bankName, accountNo, accountAlias, description);
            System.out.println("✅ 은행 계좌 등록 요청 완료.");
        } else {
            throw new IllegalArgumentException("잘못된 서브 메뉴 선택입니다.");
        }
    }

    // 2. 조회 기능 분기
    private void search() throws SQLException {
        while(true) {
            System.out.println("\n--- 재무 정보 조회 ---");
            System.out.println("1. 계정 목록 조회 ");
            System.out.println("2. 은행계좌 목록 조회");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 > ");

            String subMenu = sc.nextLine();

            if ("0".equals(subMenu)) break;

            if (subMenu.equals("1")) {
                List<AccountDTO> accountInfo = financeService.getAllActiveAccounts();
                showAccountInfo(accountInfo);
            } else if (subMenu.equals("2")) {
                List<BankAccountDTO> bankAccountInfo = financeService.selectBankAccountInfo();
                showBankAccountInfo(bankAccountInfo);
            } else {
                throw new IllegalArgumentException("잘못된 선택입니다.");
            }
        }
    }

    // 3. 계정 관리 서브 메뉴 (삭제/복구/목록)
    private void manageAccountMasterData() throws SQLException {
        while(true) {
            System.out.println("\n[계정 관리 (Soft Delete)]");
            System.out.println("1. 계정 사용 중지");
            System.out.println("2. 비활성 계정 목록 조회");
            System.out.println("3. 계정 복구");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 > ");
            String choice = sc.nextLine();

            if ("0".equals(choice)) break;

            if ("1".equals(choice)) {
                deactivateAccount();
            } else if ("2".equals(choice)) {
                listDeactivatedAccounts();
            } else if ("3".equals(choice)) {
                restoreAccount();
            } else {
                throw new IllegalArgumentException("잘못된 선택입니다.");
            }
        }
    }

    // [서브 기능] 계정 사용 중지
    private void deactivateAccount() throws SQLException {
        System.out.print("사용 중지할 계정 ID를 입력하세요: ");
        String accountId = sc.nextLine();

        System.out.print("정말로 이 계정을 비활성화(사용 중지) 하시겠습니까? (y/n): ");
        String confirm = sc.nextLine();

        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("🚫 작업 취소.");
            return;
        }

        financeService.deleteAccount(accountId);
        System.out.println("✅ 계정 ID [" + accountId + "] 사용 중지 완료.");
    }

    // [서브 기능] 계정 복구
    private void restoreAccount() throws SQLException {
        System.out.print("복구할 계정 ID를 입력하세요: ");
        String accountId = sc.nextLine();

        System.out.print("정말로 이 계정을 활성화(복구) 하시겠습니까? (y/n): ");
        String confirm = sc.nextLine();

        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("🚫 작업 취소.");
            return;
        }

        financeService.restoreAccount(accountId);
        System.out.println("✅ 계정 ID [" + accountId + "] 복구 완료.");
    }

    // [서브 기능] 비활성 목록 조회
    private void listDeactivatedAccounts() throws SQLException {
        List<AccountDTO> list = financeService.getAllDeactivatedAccounts();

        if (list.isEmpty()) {
            System.out.println("▶ 현재 비활성 상태의 계정이 없습니다.");
            return;
        }

        System.out.println("\n--- 비활성 계정 목록 ---");
        list.forEach(dto -> System.out.println(dto.getCode() + " | " + dto.getName() + " | ID: " + dto.getId()));
    }

    // 계정 정보 전체 조회 헬퍼
    private void showAccountInfo (List < AccountDTO > accountList) {
        System.out.printf("%-36s %-12s %-15s %-20s %-10s%n",
                "ID", "회사ID", "코드", "계정명", "구분"); // ID 너비 수정
        System.out.println("----------------------------------------------------------------------------------------------------------------------");

        for (AccountDTO a : accountList) {
            System.out.printf("%-36s %-12s %-15s %-20s %-10s%n",
                    a.getId(),
                    a.getCompanyId(),
                    a.getCode(),
                    a.getName(),
                    a.getType()
            );
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
    }

    // 은행 계좌 정보 전체 조회 헬퍼
    private void showBankAccountInfo (List < BankAccountDTO > bankAccountList) {

        System.out.printf("%-36s %-12s %-12s %-25s %-12s %-20s%n", // ID 너비 수정
                "ID", "회사ID", "은행명", "계좌번호", "별칭", "비고");

        // 구분선
        System.out.println("----------------------------------------------------------------------------------------------------------------------");

        // 데이터
        for (BankAccountDTO b : bankAccountList) {
            System.out.printf("%-36s %-12s %-12s %-25s %-12s %-20s%n",
                    b.getId(),
                    b.getCompanyId(),
                    b.getBankName(),
                    b.getAccountNo(),
                    b.getAccountAlias(),
                    b.getDescription()
            );
        }
        System.out.println("----------------------------------------------------------------------------------------------------------------------");
    }

    /**
     * 예산 대비 실적 리포트 조회
     */
    public void showBudgetReport () throws SQLException {
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