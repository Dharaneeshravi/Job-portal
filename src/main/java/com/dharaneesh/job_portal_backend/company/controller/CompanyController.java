package com.dharaneesh.job_portal_backend.company.controller;

import com.dharaneesh.job_portal_backend.company.service.ICompanyService;
import com.dharaneesh.job_portal_backend.dto.CompanyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final ICompanyService companyService;


    @GetMapping(version = "2.0")
    public ResponseEntity<List<CompanyDto>> getCompaniesV2() {

        return ResponseEntity.ok(companyService.getAllCompanies());
    }
}
