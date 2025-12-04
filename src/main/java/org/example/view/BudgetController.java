package org.example.view;

import org.example.config.AppConfig;
import org.example.dto.BudgetDTO;
import org.example.service.BudgetService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class BudgetController {

    private final BudgetService budgetService = new BudgetService();
    private final Scanner sc;

    // ★ 자체 실행용 main 메서드
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        BudgetController controller = new BudgetController(sc);
        controller.start();
        sc.close();
        System.out.println("프로그램을 종료합니다.");
    }

    public BudgetController(Scanner sc) {
        this.sc = sc;
    }

    public void start() {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("      📊 Budget 관리 메뉴");
            System.out.println("==============================");
            System.out.println("1. 예산 등록");
            System.out.println("2. 예산 목록 조회");
            System.out.println("3. 예산 금액 수정");
            System.out.println("4. 예산 삭제");
            System.out.println("0. 종료하기");
            System.out.println("==============================");
            System.out.print("메뉴 선택: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1": registerBudget(); break;
                case "2": listBudget(); break;
                case "3": updateBudget(); break;
                case "4": deleteBudget(); break;
                case "0": return;
                default: System.out.println("❌ 잘못된 입력입니다.");
            }
        }
    }

    // 1. 등록
    private void registerBudget() {
        try {
            System.out.println("\n[예산 등록]");
            System.out.print("부서 ID 입력: "); String deptId = sc.nextLine().toUpperCase();
            System.out.print("계정 ID 입력: "); String accId = sc.nextLine();
            System.out.print("연월(yyyyMM): "); String yearMonth = sc.nextLine();
            System.out.print("예산 금액 입력: "); BigDecimal amount = new BigDecimal(sc.nextLine());

            BudgetDTO saved = budgetService.registerBudget(deptId, accId, yearMonth, amount);
            System.out.println("✅ 등록 성공!");
            System.out.println(saved);

        } catch (NumberFormatException e) {
            System.out.println("❌ 등록 실패: 금액은 숫자만 입력하세요.");
        } catch (Exception e) {
            System.out.println("❌ 등록 실패: " + e.getMessage());
        }
    }

    // 2. 조회
    private void listBudget() {
        List<BudgetDTO> list = budgetService.getBudgets(AppConfig.COMPANY_ID);

        if (list.isEmpty()) {
            System.out.println("등록된 예산이 없습니다.");
            return;
        }

        // DTO에 toString()이 잘 정의되어 있으므로 그대로 출력합니다.
        list.forEach(System.out::println);
    }

    // 3. 수정
    private void updateBudget() {
        System.out.println("\n[예산 금액 수정]");
        System.out.print("수정할 예산 ID (목록에서 복사하세요): "); String id = sc.nextLine();
        System.out.print("변경할 금액: ");

        try {
            BigDecimal newAmount = new BigDecimal(sc.nextLine());
            budgetService.updateBudgetAmount(id, newAmount);
            System.out.println("✅ 수정 완료!");
        } catch (Exception e) {
            System.out.println("❌ 수정 실패: " + e.getMessage());
        }
    }

    // 4. 삭제
    private void deleteBudget() {
        System.out.println("\n[예산 삭제]");
        System.out.print("삭제할 예산 ID (목록에서 복사하세요): "); String id = sc.nextLine();
        System.out.print("정말 삭제하시겠습니까? (y/n): ");
        if (!"y".equalsIgnoreCase(sc.nextLine())) return;

        try {
            budgetService.deleteBudget(id);
            System.out.println("✅ 삭제 완료!");
        } catch (Exception e) {
            System.out.println("❌ 삭제 실패: " + e.getMessage());
        }
    }
}