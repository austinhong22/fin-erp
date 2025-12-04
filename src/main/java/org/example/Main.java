package org.example;

import org.example.config.DBUtil;
import org.example.view.*;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws SQLException {

        // ==========================================
        // 🔍 [DB 연결 테스트 구역] 시작
        // ==========================================
        System.out.println("DB 연결을 시도합니다...");
        try (Connection conn = DBUtil.getConnection()) {
            if (conn != null) {
                System.out.println("DB 연결 성공! (객체: " + conn + ")");
            } else {
                System.out.println("DB 연결 실패 (conn is null)");
            }
        } catch (Exception e) {
            System.out.println("에러 발생! 사유:");
            e.printStackTrace();
        }
        System.out.println("==========================================\n");
        // ==========================================
        // 🔍 [DB 연결 테스트 구역] 끝
        // ==========================================

        Scanner sc = new Scanner(System.in);

        // 모든 컨트롤러 생성
        DepartmentController deptCtrl = new DepartmentController(sc);
        FinanceController financeCtrl = new FinanceController(sc);
        PartyController partyCtrl = new PartyController(sc);
        BudgetController budgetCtrl = new BudgetController(sc);
        JournalController journalCtrl = new JournalController(sc);

        while (true) {
            System.out.println("\n=== 🚀 ERP Accounting System ===");
            System.out.println("1. 기초정보 관리(부서/계정/거래처/계좌/예산)");
            System.out.println("2. 전표 입력");
            System.out.println("3. 리포트");
            System.out.println("0. 종료");
            System.out.print("선택 > ");

            String choice = sc.nextLine();

            if ("0".equals(choice)) {
                System.out.println("시스템 종료");
                break;
            } else if ("1".equals(choice)) {
                // 기초정보 관리 서브메뉴
                showBasicInfoMenu(sc, deptCtrl, financeCtrl, partyCtrl, budgetCtrl);
            } else if ("2".equals(choice)) {
                // 전표 입력
                journalCtrl.start();
            } else if ("3".equals(choice)) {
                // 리포트 (FinanceController의 조회 기능)
                showReportMenu(sc, financeCtrl);
            } else {
                System.out.println("잘못 입력했습니다. 다시 입력해주세요");
            }
        }
        sc.close();
    }

    /**
     * 기초정보 관리 서브메뉴
     */
    private static void showBasicInfoMenu(Scanner sc, DepartmentController deptCtrl,
            FinanceController financeCtrl, PartyController partyCtrl,
            BudgetController budgetCtrl) {
        while (true) {
            System.out.println("\n=== 기초정보 관리 ===");
            System.out.println("1. 부서 관리");
            System.out.println("2. 계정/계좌 관리");
            System.out.println("3. 거래처 관리");
            System.out.println("4. 예산 관리");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 > ");

            String subMenu = sc.nextLine();

            if ("0".equals(subMenu)) {
                break;
            } else if ("1".equals(subMenu)) {
                deptCtrl.start();
            } else if ("2".equals(subMenu)) {
                financeCtrl.start(); // 계정/계좌 등록 기능
            } else if ("3".equals(subMenu)) {
                partyCtrl.start();
            } else if ("4".equals(subMenu)) {
                budgetCtrl.start();
            } else {
                System.out.println("잘못 입력했습니다. 다시 입력해주세요");
            }
        }
    }

    /**
     * 리포트 메뉴
     */
    private static void showReportMenu(Scanner sc, FinanceController financeCtrl) throws SQLException {
        while (true) {
            System.out.println("\n=== 리포트 ===");
            System.out.println("1. 예산 대비 실적 리포트");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 > ");

            String reportMenu = sc.nextLine();

            if ("0".equals(reportMenu)) {
                break;
            } else if ("1".equals(reportMenu)) {
                // FinanceController의 리포트 조회 기능 직접 호출
                financeCtrl.showBudgetReport();
            } else {
                System.out.println("잘못 입력했습니다. 다시 입력해주세요");
            }
        }
    }
}