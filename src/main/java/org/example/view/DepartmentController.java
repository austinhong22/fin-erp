package org.example.view;

import org.example.config.AppConfig;
import org.example.dto.DepartmentDTO;
import org.example.service.DepartmentService;

import java.util.List;
import java.util.Scanner;

public class DepartmentController {

    private final DepartmentService departmentService = new DepartmentService();
    private final Scanner sc;

    public static void main(String[] args) {
        // 1. Scanner 객체 생성
        Scanner sc = new Scanner(System.in);

        // 2. DepartmentController 인스턴스 생성
        DepartmentController controller = new DepartmentController(sc);

        // 3. start() 메서드 바로 호출
        controller.start();

        // 4. 종료 처리
        sc.close();
        System.out.println("프로그램을 종료합니다.");
    }
    // ===============================================

    public DepartmentController(Scanner sc) {
        this.sc = sc;
    }

    public void start() {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("    🏢 Department 관리 메뉴 (단독 실행)");
            System.out.println("==============================");
            System.out.println("1. 부서 등록");
            System.out.println("2. 부서 목록 조회");
            System.out.println("0. 뒤로가기");
            System.out.println("==============================");
            System.out.print("메뉴 선택: ");

            String choice = sc.nextLine();

            switch (choice) {
                case "1": registerDepartment(); break;
                case "2": listDepartments(); break;
                case "0": return;
                default: System.out.println("❌ 잘못된 입력입니다.");
            }
        }
    }

    private void registerDepartment() {
        try {
            System.out.println("\n[부서 등록]");

            System.out.print("부서명 입력: ");
            String name = sc.nextLine();

            System.out.print("부서 코드 입력 (예: DEV, HR): ");
            String code = sc.nextLine();

            DepartmentDTO saved = departmentService.registerDepartment(name, code);

            System.out.println("✅ 등록 성공: " + saved);

        } catch (Exception e) {
            System.out.println("❌ 등록 실패: " + e.getMessage());
        }
    }

    private void listDepartments() {
        System.out.println("\n[부서 목록 조회]");

        List<DepartmentDTO> list = departmentService.getDepartments(AppConfig.COMPANY_ID);
        if (list.isEmpty()) {
            System.out.println("등록된 부서가 없습니다.");
            return;
        }

        list.forEach(System.out::println);
    }
}