package org.example.view;

import org.example.config.AppConfig;
import org.example.dto.DepartmentDTO;
import org.example.service.DepartmentService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DepartmentController {

    private final DepartmentService departmentService = new DepartmentService();
    private final Scanner sc;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        new DepartmentController(sc).start();
        sc.close();
        System.out.println("프로그램을 종료합니다.");
    }

    public DepartmentController(Scanner sc) {
        this.sc = sc;
    }

    public void start() {
        while (true) {
            System.out.println("\n==============================");
            System.out.println("    🏢 Department 관리 메뉴");
            System.out.println("==============================");
            System.out.println("1. 부서 등록");
            System.out.println("2. 부서 목록 조회");
            System.out.println("3. 부서 정보 수정");
            System.out.println("4. 비활성 부서 목록 조회");
            System.out.println("5. 부서 사용 중지");
            System.out.println("6. 비활성 부서 복구");
            System.out.println("0. 종료");
            System.out.println("==============================");
            System.out.print("선택 > ");

            String choice = sc.nextLine();

            try {
                switch (choice) {
                    case "1": registerDepartment(); break;
                    case "2": listDepartments(); break;
                    case "3": updateDepartment(); break;
                    case "4": listDeactivatedDepartments(); break;
                    case "5": deleteDepartment(); break;
                    case "6": restoreDepartment(); break;
                    case "0": return;
                    default: System.out.println("❌ 잘못된 입력입니다.");
                }
            } catch (SQLException e) {
                System.out.println("❌ 시스템 오류: 데이터베이스 접속/처리 중 오류가 발생했습니다.");
            } catch (Exception e) {
                System.out.println("❌ 작업 실패: " + e.getMessage());
            }
        }
    }

    private void registerDepartment() throws SQLException {
        try {
            System.out.println("\n[부서 등록]");
            System.out.print("부서명 입력: ");
            String name = sc.nextLine();
            System.out.print("부서 코드 입력: ");
            String code = sc.nextLine();

            DepartmentDTO saved = departmentService.registerDepartment(name, code);
            System.out.println("✅ 등록 성공: " + saved);

        } catch (IllegalArgumentException e) {
            System.out.println("❌ 등록 실패: " + e.getMessage());
        }
    }

    private void listDepartments() throws SQLException {
        try {
            System.out.println("\n[부서 목록 조회]");

            List<DepartmentDTO> list = departmentService.getDepartments(AppConfig.COMPANY_ID);

            if (list.isEmpty()) {
                System.out.println("등록된 활성 부서가 없습니다.");
                return;
            }

            list.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 조회 실패: " + e.getMessage());
        }
    }

    private void updateDepartment() throws SQLException {
        System.out.println("❌ 수정 기능 구현 필요.");
    }

    private void listDeactivatedDepartments() throws SQLException {
        try {
            System.out.println("\n[비활성 부서 목록 조회]");

            List<DepartmentDTO> list = departmentService.getDeactivatedDepartments(AppConfig.COMPANY_ID);

            if (list.isEmpty()) {
                System.out.println("비활성 상태의 부서가 없습니다.");
                return;
            }

            System.out.println("--- 비활성 상태 부서 목록 ---");
            list.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 조회 실패: " + e.getMessage());
        }
    }

    private void deleteDepartment() throws SQLException {
        try {
            System.out.println("\n[부서 사용 중지]");
            System.out.print("사용 중지할 부서 ID: ");
            String id = sc.nextLine();

            System.out.print("정말 중지하시겠습니까? (y/n): ");
            String confirm = sc.nextLine();

            if (!"y".equalsIgnoreCase(confirm)) {
                System.out.println("🚫 작업 취소.");
                return;
            }

            departmentService.deleteDepartment(id);
            System.out.println("✅ 부서 사용 중지 완료 (데이터는 DB에 보존됨).");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 중지 실패: " + e.getMessage());
        }
    }

    private void restoreDepartment() throws SQLException {
        try {
            System.out.println("\n[비활성 부서 복구]");
            System.out.print("복구할 부서 ID를 입력하세요: ");
            String id = sc.nextLine();

            System.out.print("정말로 이 부서를 활성화(복구) 하시겠습니까? (y/n): ");
            String confirm = sc.nextLine();

            if (!"y".equalsIgnoreCase(confirm)) {
                System.out.println("🚫 작업 취소.");
                return;
            }

            departmentService.restoreDepartment(id);
            System.out.println("✅ 부서 복구 완료: ID [" + id + "]가 다시 활성 상태가 되었습니다.");
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 복구 실패: " + e.getMessage());
        }
    }
}