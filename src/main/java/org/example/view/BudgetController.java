package org.example.view;

import org.example.config.AppConfig;
import org.example.dto.BudgetDTO;
import org.example.service.BudgetService;

import java.math.BigDecimal; // ★ BigDecimal import
import java.util.List;
import java.util.Scanner;

public class BudgetController {

    private final BudgetService budgetService = new BudgetService();
    private final Scanner sc;

    public static void main(String[] args) {
        // 1. Scanner 객체 생성
        Scanner sc = new Scanner(System.in);

        // 2. BudgetController 인스턴스 생성
        BudgetController controller = new BudgetController(sc);

        // 3. start() 메서드 바로 호출
        controller.start();

        // 4. 종료 처리
        sc.close();
        System.out.println("프로그램을 종료합니다.");
    }

    public BudgetController(Scanner sc) {
        this.sc = sc;
    }

    public void start() {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("      📊 Budget 관리 메뉴 (단독 실행)");
            System.out.println("==============================");
            System.out.println("1. 예산 등록");
            System.out.println("2. 예산 목록 조회");
            System.out.println("0. 뒤로가기");
            System.out.println("==============================");
            System.out.print("메뉴 선택: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1": registerBudget(); break;
                case "2": listBudget(); break;
                case "0": return;
                default: System.out.println("❌ 잘못된 입력입니다.");
            }
        }
    }

    private void registerBudget() {
        try {
            System.out.println("\n[예산 등록]");

            System.out.print("부서 ID 입력: ");
            String deptId = sc.nextLine().toUpperCase();

            // 참고: 계정 ID 유효성 검증은 Service에서 처리됩니다.
            System.out.print("계정 ID 입력: ");
            String accId = sc.nextLine();

            System.out.print("연월 입력 (yyyyMM): ");
            String yearMonth = sc.nextLine();

            System.out.print("예산 금액 입력: ");
            String amountStr = sc.nextLine();

            // ★ String을 BigDecimal로 파싱
            BigDecimal amount = new BigDecimal(amountStr);

            // ★ BigDecimal 타입으로 Service 호출
            BudgetDTO saved = budgetService.registerBudget(deptId, accId, yearMonth, amount);

            System.out.println("✅ 등록 성공!");
            System.out.println(saved);

        } catch (NumberFormatException e) {
            // 사용자가 숫자가 아닌 값을 입력했을 때
            System.out.println("❌ 등록 실패: 금액은 유효한 숫자 형식이어야 합니다.");
        } catch (Exception e) {
            // Service에서 발생한 유효성 검사 및 중복 예외 처리
            System.out.println("❌ 등록 실패: " + e.getMessage());
        }
    }

    private void listBudget() {

        List<BudgetDTO> list = budgetService.getBudgets(AppConfig.COMPANY_ID);

        if (list.isEmpty()) {
            System.out.println("등록된 예산이 없습니다.");
            return;
        }

        // DTO에 toString()이 잘 정의되어 있으므로 그대로 출력합니다.
        list.forEach(System.out::println);
    }
}