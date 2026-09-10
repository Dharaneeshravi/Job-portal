package com.dharaneesh.job_portal_backend.company.service;

import com.dharaneesh.job_portal_backend.dto.CompanyDto;
import java.util.List;

public interface ICompanyService {
    List<CompanyDto> getAllCompanies();
}
