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

    // 자체 실행용 main 메서드 (단독 테스트용)
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
            System.out.println("4. 부서 사용 중지 (삭제)");
            System.out.println("0. 뒤로가기");
            System.out.println("==============================");
            System.out.print("메뉴 선택: ");

            String choice = sc.nextLine();

            // Service가 throws SQLException을 던지므로, Controller에서 최종적으로 잡습니다.
            try {
                switch (choice) {
                    case "1": registerDepartment(); break;
                    case "2": listDepartments(); break;
                    case "3": updateDepartment(); break;
                    case "4": deleteDepartment(); break;
                    case "0": return;
                    default: System.out.println("❌ 잘못된 입력입니다.");
                }
            } catch (SQLException e) {
                // DB 관련 오류 발생 시 시스템 오류로 안내
                System.out.println("❌ 시스템 오류: 데이터베이스 접속/처리 중 오류가 발생했습니다.");
                // 개발 단계에서는 e.printStackTrace()를 추가하여 오류 원인을 확인할 수 있습니다.
            } catch (Exception e) {
                // Service에서 던진 IllegalArgumentException 등 비즈니스 오류 처리
                System.out.println("❌ 작업 실패: " + e.getMessage());
            }
        }
    }

    // 1. 등록
    private void registerDepartment() throws SQLException {
        try {
            System.out.println("\n[부서 등록]");
            System.out.print("부서명 입력: ");
            String name = sc.nextLine();
            System.out.print("부서 코드 입력 (예: DEV, HR): ");
            String code = sc.nextLine();

            // Service 호출 (Service 내부에서 DAO 호출 시 SQLException 발생 가능)
            DepartmentDTO saved = departmentService.registerDepartment(name, code);
            System.out.println("✅ 등록 성공: " + saved);

        } catch (IllegalArgumentException e) { // 비즈니스 오류는 친절하게 출력
            System.out.println("❌ 등록 실패: " + e.getMessage());
        }
    }

    // 3. 수정
    private void updateDepartment() throws SQLException {
        // [TODO] Service의 updateDepartment 메서드가 throws SQLException을 던지도록 수정 필요.
        System.out.println("❌ [TODO] 수정 기능 구현 필요.");
        // (구현 시 Service 호출 후 상위 try-catch로 SQLException이 전파됨)
    }

    // 4. 삭제 기능 (Soft Delete 호출)
    private void deleteDepartment() throws SQLException {
        try {
            System.out.println("\n[부서 사용 중지 (Soft Delete)]");
            System.out.print("사용 중지할 부서 ID: ");
            String id = sc.nextLine();

            System.out.print("정말 중지하시겠습니까? (y/n): ");
            String confirm = sc.nextLine();

            if (!"y".equalsIgnoreCase(confirm)) {
                System.out.println("🚫 작업 취소.");
                return;
            }

            // Service 호출 (Service 내부에서 softDeleteById 호출 시 SQLException 발생 가능)
            departmentService.deleteDepartment(id);
            System.out.println("✅ 부서 사용 중지 완료 (데이터는 DB에 보존됨).");
        } catch (IllegalArgumentException e) {
            // ID 없음 등의 비즈니스 오류 처리
            System.out.println("❌ 중지 실패: " + e.getMessage());
        }
    }

    // 2. 조회
    private void listDepartments() throws SQLException {
        try {
            System.out.println("\n[부서 목록 조회]");

            // Service 호출 (Service 내부에서 SQLException 처리 필요)
            List<DepartmentDTO> list = departmentService.getDepartments(AppConfig.COMPANY_ID);

            if (list.isEmpty()) {
                System.out.println("등록된 부서가 없습니다.");
                return;
            }

            list.forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ 조회 실패: " + e.getMessage());
        }
    }
}