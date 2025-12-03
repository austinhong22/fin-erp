package org.example;

import org.example.dto.Company;
import org.example.dto.Department;
import org.example.service.CompanyService;
import org.example.service.DepartmentService;

import java.util.List;

public class TestRunner {

    public static void main(String[] args) {

        // 서비스 준비
        CompanyService companyService = new CompanyService();
        DepartmentService departmentService = new DepartmentService();

        System.out.println("===== 초기 회사 / 부서 테스트 시작 =====\n");

        // ---------------------------------------------------
        // 1) 회사 등록
        // ---------------------------------------------------
        Company c1 = companyService.registerCompany(
                "삼성전자",
                "123-45-67890"
        );

        Company c2 = companyService.registerCompany(
                "네이버",
                "987-65-43210"
        );

        System.out.println("📌 회사 등록 결과:");
        System.out.println(c1);
        System.out.println(c2);
        System.out.println();

        // ---------------------------------------------------
        // 2) 부서 등록 (회사 ID 반드시 사용!)
        // ---------------------------------------------------
        Department d1 = departmentService.registerDepartment(
                c1.getId(),      // 삼성전자 ID
                "개발팀",
                "dev"
        );

        Department d2 = departmentService.registerDepartment(
                c1.getId(),      // 삼성전자 ID
                "인사팀",
                "hr"
        );

        Department d3 = departmentService.registerDepartment(
                c2.getId(),      // 네이버 ID
                "플랫폼팀",
                "pf"
        );

        System.out.println("📌 부서 등록 결과:");
        System.out.println(d1);
        System.out.println(d2);
        System.out.println(d3);
        System.out.println();

        // ---------------------------------------------------
        // 3) 회사별 부서 조회
        // ---------------------------------------------------
        System.out.println("📌 삼성전자 부서 목록:");
        departmentService.getDepartmentsByCompanyId(c1.getId())
                .forEach(System.out::println);

        System.out.println("\n📌 네이버 부서 목록:");
        departmentService.getDepartmentsByCompanyId(c2.getId())
                .forEach(System.out::println);
        System.out.println("회사 목록");
        List<Company> conmpaines = companyService.getAllCompanies();
        for (Company c : conmpaines ){
            System.out.println(c);}
        List<Department> samsungList = departmentService.getDepartmentsByCompanyId(samsung.getId());
        departmentService.printSimpleDepartments(samsungList);
        for (Department d : samsungList) {
            System.out.println(d.getCode() + " - " + d.getName());
        }
        // ---------------------------------------------------
        System.out.println("\n===== 테스트 완료 =====");
    }
}
