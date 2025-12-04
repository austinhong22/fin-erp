package org.example.view;

import org.example.dto.PartyDTO;
import org.example.dto.PartyLedgerDTO;
import org.example.service.PartyService;


import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class PartyController {

    private final Scanner sc;
    private final PartyService service;

    // 생성자: Main 또는 상위 Manager에서 스캐너를 받아옵니다.
    public PartyController(Scanner sc) {
        this.sc = sc;
        this.service = new PartyService();
    }

    // 진입점 메서드
    public void start() {
        while (true) {
            System.out.println("\n=== 거래처 관리 (Party Management) ===");
            System.out.println("1. 거래처 등록");
            System.out.println("2. 거래처 목록 조회 ");
            System.out.println("3. 거래처 원장 조회 ");
            System.out.println("4. 거래처 사용 중지 ");
            System.out.println("5. 비활성 목록 조회");
            System.out.println("6. 거래처 복구 ");
            System.out.println("0. 종료");
            System.out.print("선택 > ");

            String menu = sc.nextLine();
            if ("0".equals(menu)) break;

            try {
                if ("1".equals(menu)) register();
                else if ("2".equals(menu)) searchMenu();
                else if ("3".equals(menu)) viewLedger();
                else if ("4".equals(menu)) deactivateParty();
                else if ("5".equals(menu)) listDeactivatedParties();
                else if ("6".equals(menu)) restoreParty();
                else System.out.println("잘못 입력했습니다. 다시 입력해주세요.");
            } catch (SQLException e) {
                // SQL 예외 처리
                System.out.println("DB 작업 중 오류가 발생했습니다: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                // Service에서 던진 입력값 유효성 검사 예외 처리
                System.out.println("입력 오류: " + e.getMessage());
            } catch (Exception e) {
                // 일반 예외 처리
                System.out.println("오류 발생: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    //  1. 거래처 등록 (Create)
    private void register() throws SQLException {
        System.out.println("\n[거래처 등록 정보 입력]");
        System.out.print("이름: ");
        String name = sc.nextLine();
        System.out.print("유형 (CUSTOMER/VENDOR): ");
        String type = sc.nextLine();
        System.out.print("연락처: ");
        String contact = sc.nextLine();
        System.out.print("사업자 등록 번호: ");
        String regNo = sc.nextLine();

        // DTO 생성 및 데이터 설정
        PartyDTO dto = new PartyDTO();
        dto.setName(name);
        dto.setType(type);
        dto.setContact(contact);
        dto.setRegistrationNumber(regNo);

        // Service 호출 (등록)
        service.registerParty(dto.getName(), dto.getType(), dto.getContact(), dto.getRegistrationNumber());
        System.out.println("거래처 등록 요청 완료.");
    }

    //  2. 조회/검색 메뉴 분기 (search 메서드 통합)
    private void searchMenu() throws SQLException {
        while(true) {
            System.out.println("\n[거래처 조회/검색]");
            System.out.println("1. 전체 목록 조회");
            System.out.println("2. 이름 키워드 검색");
            System.out.println("0. 이전 메뉴로");
            System.out.print("선택 > ");
            String choice = sc.nextLine();

            List<PartyDTO> list;
            String title;

            if ("0".equals(choice)) break;

            try {
                if ("1".equals(choice)) {
                    // 전체 조회 (활성 상태만)
                    list = service.getAllParties();
                    title = "전체 목록 (활성 상태)";
                } else if ("2".equals(choice)) {
                    // 이름 검색
                    System.out.print("검색할 거래처 이름 (키워드): ");
                    String keyword = sc.nextLine();
                    list = service.searchParties(keyword);
                    title = "'" + keyword + "' 검색 결과";
                } else {
                    System.out.println("잘못 입력했습니다. 다시 입력해주세요.");
                    continue;
                }

                displayPartyList(list, title); // 출력 헬퍼 메서드 호출

            } catch (IllegalArgumentException e) {
                System.out.println("입력 오류: " + e.getMessage());
            } catch (SQLException e) {
                System.out.println("DB 작업 중 오류가 발생했습니다: " + e.getMessage());
            }
        }
    }

    //  3. 거래처 원장 조회 (핵심 미션) - 완성
    private void viewLedger() throws SQLException {
        System.out.print("\n조회할 거래처 ID를 입력하세요: "); // 거래처 ID 입력 요청
        String partyId = sc.nextLine();

        // Service 호출 (PartyLedgerDTO 리스트 반환)
        List<PartyLedgerDTO> ledgerLines = service.getLedgerLinesByPartyId(partyId);

        // 출력 헬퍼 메서드 호출
        displayPartyLedger(partyId, ledgerLines);
    }

    // 4. 거래처 사용 중지 (Soft Delete) 메서드 구현
    private void deactivateParty() throws SQLException {
        System.out.println("\n[거래처 사용 중지]");
        System.out.print("사용 중지할 거래처 ID: ");
        String id = sc.nextLine();

        System.out.print("정말로 이 거래처를 비활성화(사용 중지) 하시겠습니까? (y/n): ");
        String confirm = sc.nextLine();

        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("비활성화 취소.");
            return;
        }

        service.deleteParty(id); // Service 호출 (내부적으로 softDeleteById 실행)
        System.out.println("거래처 ID [" + id + "] 사용 중지 완료 (데이터는 보존됨).");
    }

    // ★ 5. 비활성 거래처 목록 조회 (새 기능) 구현
    private void listDeactivatedParties() throws SQLException {
        System.out.println("\n[비활성 거래처 목록 조회]");

        List<PartyDTO> list = service.getAllDeactivatedParties();

        if (list.isEmpty()) {
            System.out.println("▶ 현재 비활성 상태의 거래처가 없습니다.");
            return;
        }

        displayPartyList(list, "비활성 목록");
    }

    // ★ 6. 거래처 복구 (Restore) 기능 구현
    private void restoreParty() throws SQLException {
        System.out.println("\n[거래처 복구]");
        System.out.print("복구할 거래처 ID를 입력하세요: ");
        String id = sc.nextLine();

        System.out.print("정말로 이 거래처를 활성화(복구) 하시겠습니까? (y/n): ");
        String confirm = sc.nextLine();

        if (!"y".equalsIgnoreCase(confirm)) {
            System.out.println("🚫 복구 취소.");
            return;
        }

        service.restoreParty(id); // Service 호출 (내부적으로 restoreById 실행)
        System.out.println("✅ 거래처 ID [" + id + "] 복구 완료 (다시 사용 가능).");
    }


    // [Helper] 거래처 목록 출력 기능
    private void displayPartyList(List<PartyDTO> parties, String title) {
        if (parties == null || parties.isEmpty()) {
            System.out.println("▶ " + title + "에 해당하는 등록된 거래처가 없습니다.");
            return;
        }
        System.out.println("\n[거래처 목록 (" + title + ", 총 " + parties.size() + "건)]");
        System.out.println("------------------------------------------------------------------");

        System.out.printf("| %-36s | %-15s | %-8s | %-12s |\n", "ID", "이름", "유형", "사업자번호");
        System.out.println("------------------------------------------------------------------");
        for (PartyDTO party : parties) {
            System.out.printf("| %-36s | %-15s | %-8s | %-12s |\n",
                    party.getId(), party.getName(), party.getType(), party.getRegistrationNumber());
        }
        System.out.println("------------------------------------------------------------------");
    }

    //  [Helper] 거래처 원장 상세 내역 출력 기능 (I/O)
    private void displayPartyLedger(String partyId, List<PartyLedgerDTO> lines) {
        if (lines.isEmpty()) {
            System.out.println("▶ 해당 거래처(ID: " + partyId + ")의 거래 내역이 없습니다.");
            return;
        }
        System.out.println("\n[거래처 원장 상세 (ID: " + partyId + ", 총 " + lines.size() + "건)]");
        System.out.println("[거래처 사업자 등록번호 ( " + lines.get(0).getRegistration() + " )");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        System.out.printf("| %-10s | %-20s | %-10s | %10s | %10s |\n", "날짜", "계정", "적요", "차변 금액", "대변 금액");
        System.out.println("--------------------------------------------------------------------------------------------------------");
        //  PartyLedgerDTO 사용
        for (PartyLedgerDTO line : lines) {
            // BigDecimal을 doubleValue()로 변환하여 printf 정렬 포맷에 맞춤
            System.out.printf("| %-10s | %-20s | %-10s | %10.0f | %10.0f |\n",
                    line.getEntryDate(),    // 날짜
                    line.getGlAccountName(),// 계정 이름
                    line.getDescription(),  // 적요 (JournalEntry의 Description)
                    line.getDebitAmount().doubleValue(),
                    line.getCreditAmount().doubleValue());
        }
        System.out.println("--------------------------------------------------------------------------------------------------------");
    }
}