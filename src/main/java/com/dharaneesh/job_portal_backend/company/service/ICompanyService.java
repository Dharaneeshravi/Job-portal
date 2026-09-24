package com.dharaneesh.job_portal_backend.company.service;

import com.dharaneesh.job_portal_backend.dto.CompanyDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public interface ICompanyService {
    List<CompanyDto> getAllCompanies();

    boolean createCompany( CompanyDto companyDto);

    List<CompanyDto> getAllCompanyForAdmin();

    boolean updateCompanyDetails( Long id,  CompanyDto companyDto);

    void deleteCompanyDetails( Long id);
}
