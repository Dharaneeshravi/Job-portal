package com.dharaneesh.job_portal_backend.company.controller;

import com.dharaneesh.job_portal_backend.company.service.ICompanyService;
import com.dharaneesh.job_portal_backend.dto.CompanyDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final ICompanyService companyService;


    @GetMapping(version = "1.0")
    public ResponseEntity<List<CompanyDto>> getCompaniesV2() {

        return ResponseEntity.ok(companyService.getAllCompanies());
    }

    @PostMapping(path = "/admin",version = "1.0")
    public ResponseEntity<String> createCompany(@RequestBody @Valid CompanyDto companyDto) {

        boolean isCreated=companyService.createCompany(companyDto);

        if(isCreated){
            return ResponseEntity.ok("Company created successfully");
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to create company");
        }
    }

    @GetMapping(path = "/admin" ,version = "1.0")
    public ResponseEntity<List<CompanyDto>> getAllCompanyForAdmin() {

        return ResponseEntity.ok(companyService.getAllCompanyForAdmin());
    }

    @PutMapping(path = "/{id}/admin",version = "1.0")
    public ResponseEntity<String> updateCompanyDetails(@RequestBody @Valid CompanyDto companyDto, @PathVariable @NotBlank(message = "company id not null") Long id) {

        boolean isUpdated=companyService.updateCompanyDetails(id,companyDto);

        if(isUpdated){
            return ResponseEntity.ok("Company updated successfully");
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update company");
        }
    }

    @DeleteMapping(path = "/{id}/admin",version = "1.0")
    public ResponseEntity<String> deleteCompanyDetails(@PathVariable @NotBlank(message = "company id not null") Long id) {

            companyService.deleteCompanyDetails(id);
            return ResponseEntity.ok("Company deleted successfully");
    }
}
