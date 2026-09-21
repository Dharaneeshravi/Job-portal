package com.dharaneesh.job_portal_backend.company.service.impl;

import com.dharaneesh.job_portal_backend.company.service.ICompanyService;
import com.dharaneesh.job_portal_backend.dto.JobDto;
import com.dharaneesh.job_portal_backend.entity.Company;
import com.dharaneesh.job_portal_backend.entity.Job;
import com.dharaneesh.job_portal_backend.repository.CompanyRepository;
import com.dharaneesh.job_portal_backend.dto.CompanyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CompanyServiceImpl implements ICompanyService {

    private final CompanyRepository companyRepository;


    @Override
    public List<CompanyDto> getAllCompanies() {

        List<Company> companyList=companyRepository.findAllWithJobStatus("ACTIVE");
       return companyList.stream().map(this::transferCompanyToDto).toList();
    }

    private CompanyDto transferCompanyToDto(Company company) {

        List<JobDto> jobDtoList=company.getJobs().stream().map(this::transferJobToDto)
                .toList();

        return new CompanyDto(company.getId(),company.getName(),company.getLogo(),
                company.getIndustry(), company.getSize(),company.getRating(),company.getLocations(),
                company.getFounded(),company.getDescription(),company.getEmployees(),company.getWebsite(),
                company.getCreatedAt(),jobDtoList);
    }

    private JobDto transferJobToDto(Job job) {

        return new JobDto(
                job.getId(),
                job.getTitle(),
                job.getCompany().getId(),
                job.getCompany().getName(),
                job.getCompany().getLogo(),
                job.getLocation(),
                job.getWorkType(),
                job.getJobType(),
                job.getCategory(),
                job.getExperienceLevel(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getSalaryCurrency(),
                job.getSalaryPeriod(),
                job.getDescription(),
                job.getRequirements(),
                job.getBenefits(),
                job.getPostedDate(),
                job.getApplicationDeadline(),
                job.getApplicationsCount(),
                job.getFeatured(),
                job.getUrgent(),
                job.getRemote(),
                job.getStatus()
        );
    }
}
