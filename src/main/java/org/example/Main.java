package org.example;

import org.example.config.DBUtil;

import java.sql.Connection;
import java.util.Scanner;


public class Main {
    public static void main(String[] args) {

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

        while (true) {
            System.out.println("\n=== ERP Accounting System ===");
            System.out.println("1. 기초정보 관리(부서/계정/거래처/계좌)");
            System.out.println("2. 전표 입력");
            System.out.println("3. 리포트");
            System.out.println("0. 종료");
            System.out.print("선택 > ");

            String choice = sc.nextLine();

            if ("0".equals(choice)) {
                System.out.println("시스템 종료");
                break;
            } else if ("1".equals(choice)) {
                System.out.println("기초정보관리 시스템");
            } else if ("2".equals(choice)) {
                System.out.println("전표입력 기능");
            } else if ("3".equals(choice)) {
                System.out.println("리포트 기능");
            } else {
                System.out.println("잘못 입력했습니다. 다시 입력해주세요");
            }
        }
        sc.close();
    }
}