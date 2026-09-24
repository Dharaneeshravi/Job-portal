package com.dharaneesh.job_portal_backend.job.service.impl;

import com.dharaneesh.job_portal_backend.dto.JobDto;
import com.dharaneesh.job_portal_backend.entity.JobPortalUser;
import com.dharaneesh.job_portal_backend.job.service.IJobService;
import com.dharaneesh.job_portal_backend.repository.JobPortalUserRepository;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Server
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements IJobService {

    private final JobPortalUserRepository jobPortalUserRepository;

    @Override
    public List<JobDto> getEmployerJobs(String employerEmail) {

        JobPortalUser employer=jobPortalUserRepository.findByEmail(employerEmail)
                .orElseThrow(()->new RuntimeException("Employer not found"));

        if(employer.getCompany()==null)
        {
            throw new RuntimeException("Employer does not have company");
        }
    }
}
