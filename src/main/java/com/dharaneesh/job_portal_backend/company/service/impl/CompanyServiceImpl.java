package com.dharaneesh.job_portal_backend.company.service.impl;

import com.dharaneesh.job_portal_backend.company.service.ICompanyService;
import com.dharaneesh.job_portal_backend.dto.JobDto;
import com.dharaneesh.job_portal_backend.entity.Company;
import com.dharaneesh.job_portal_backend.entity.Job;
import com.dharaneesh.job_portal_backend.repository.CompanyRepository;
import com.dharaneesh.job_portal_backend.dto.CompanyDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
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

    @Override
    @Transactional
    public boolean createCompany(CompanyDto companyDto) {

        Company company=tarnsferCompanyDtoToCompany(companyDto);
        Company savedCompany=companyRepository.save(company);
        return savedCompany.getId()!=null && savedCompany.getId()>0;
    }

    @Cacheable("companies")
    @Override
    public List<CompanyDto> getAllCompanyForAdmin() {

        List<Company> companyList = companyRepository.findAll();
        return companyList.stream().map(this::transferCompanyToDtoForAdmin).toList();
    }

    @Override
    @Transactional
    public boolean updateCompanyDetails(Long id, CompanyDto companyDto) {

        int updatedCompany=companyRepository.updateCompanyDetails(
                id,companyDto.name(),companyDto.logo(),companyDto.industry(),
                companyDto.size(),companyDto.rating(),companyDto.locations(),
                companyDto.founded(),companyDto.description(),companyDto.employees(),companyDto.website()
        );

        return updatedCompany>0;
    }

    @Override
    @Transactional
    public void deleteCompanyDetails(Long id) {

        companyRepository.deleteById(id);
    }

    private CompanyDto transferCompanyToDtoForAdmin(Company company) {

        return new CompanyDto(company.getId(),company.getName(),company.getLogo(),
                company.getIndustry(), company.getSize(),company.getRating(),company.getLocations(),
                company.getFounded(),company.getDescription(),company.getEmployees(),company.getWebsite(),
                company.getCreatedAt(),null);
    }

    private Company tarnsferCompanyDtoToCompany(CompanyDto companyDto) {

        Company company=new Company();
        BeanUtils.copyProperties(companyDto, company);
        return company;
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
