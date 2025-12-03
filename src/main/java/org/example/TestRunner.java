package org.example;

import org.example.dto.CompanyDTO;
import org.example.dto.DepartmentDTO;
import org.example.service.CompanyService;
import org.example.service.DepartmentService;

import java.util.List;

public class TestRunner {

    public static void main(String[] args) {

        // 서비스 생성
        CompanyService companyService = new CompanyService();
        DepartmentService departmentService = new DepartmentService();

        System.out.println("===== JDBC 기능 테스트 시작 =====\n");

        // ---------------------------------------------------------
        // 1) 회사 등록 테스트
        // ---------------------------------------------------------
        CompanyDTO c1 = companyService.registerCompany("삼성전자", "123-45-67890");
        CompanyDTO c2 = companyService.registerCompany("네이버", "987-65-43210");

        System.out.println("📌 회사 등록 완료:");
        System.out.println(c1);
        System.out.println(c2);
        System.out.println();


        // ---------------------------------------------------------
        // 2) 부서 등록 테스트 (회사 ID 사용)
        // ---------------------------------------------------------
        DepartmentDTO d1 = departmentService.registerDepartment(
                c1.getId(),
                "개발팀",
                "dev"
        );

        DepartmentDTO d2 = departmentService.registerDepartment(
                c1.getId(),
                "인사팀",
                "hr"
        );

        DepartmentDTO d3 = departmentService.registerDepartment(
                c2.getId(),
                "플랫폼팀",
                "pf"
        );

        System.out.println("📌 부서 등록 완료:");
        System.out.println(d1);
        System.out.println(d2);
        System.out.println(d3);
        System.out.println();


        // ---------------------------------------------------------
        // 3) 회사별 부서 조회 테스트
        // ---------------------------------------------------------
        System.out.println("📌 삼성전자 부서 목록:");
        List<DepartmentDTO> samsungDeptList =
                departmentService.getDepartmentsByCompanyId(c1.getId());
        samsungDeptList.forEach(System.out::println);

        System.out.println("\n📌 네이버 부서 목록:");
        List<DepartmentDTO> naverDeptList =
                departmentService.getDepartmentsByCompanyId(c2.getId());
        naverDeptList.forEach(System.out::println);
        System.out.println();


        // ---------------------------------------------------------
        // 4) 전체 회사 목록 조회
        // ---------------------------------------------------------
        System.out.println("📌 전체 회사 목록:");
        List<CompanyDTO> allCompanies = companyService.getAllCompanies();
        allCompanies.forEach(System.out::println);


        System.out.println("\n===== 테스트 완료! =====");
    }
}
