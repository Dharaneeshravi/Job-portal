package com.dharaneesh.job_portal_backend.job.service.impl;

import com.dharaneesh.job_portal_backend.dto.JobDto;
import com.dharaneesh.job_portal_backend.entity.Job;
import com.dharaneesh.job_portal_backend.entity.JobPortalUser;
import com.dharaneesh.job_portal_backend.job.service.IJobService;
import com.dharaneesh.job_portal_backend.repository.JobPortalUserRepository;
import com.dharaneesh.job_portal_backend.repository.JobRepository;
import com.dharaneesh.job_portal_backend.utils.ApplicationUtils;
import io.swagger.v3.oas.annotations.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Server
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class JobServiceImpl implements IJobService {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final JobRepository jobRepository;

    @Override
    public List<JobDto> getEmployerJobs(String employerEmail) {

        JobPortalUser employer=jobPortalUserRepository.findByEmail(employerEmail)
                .orElseThrow(()->new RuntimeException("Employer not found"));

        if(employer.getCompany()==null)
        {
            throw new RuntimeException("Employer does not have company");
        }

        List<Job> jobs=employer.getCompany().getJobs();
        return jobs.stream().map(ApplicationUtils::transferJobToDto).toList();
    }

    @Override
    @Transactional
    public JobDto updateJobStatus(String jobId, String status, String employerEmail) {


        if(!status.equals("ACTIVE") && !status.equals("CLOSED") && !status.equals("DRAFT"))
        {
            throw new RuntimeException("Invalid status");
        }

        JobPortalUser employer=jobPortalUserRepository.findByEmail(employerEmail)
                .orElseThrow(()->new RuntimeException("Employer not found"));

        if(employer.getCompany()==null)
        {
            throw new RuntimeException("Employer does not have company");
        }

        Job job=employer.getCompany().getJobs().stream().filter(j->j.getId().equals(jobId)).findFirst()
                .orElseThrow(()->new RuntimeException("Job not found"));

        job.setStatus(status);
        return ApplicationUtils.transferJobToDto(job);
    }

    @Override
    @Transactional
    public JobDto createJob(JobDto jobDto, String employerEmail) {

        JobPortalUser employer=jobPortalUserRepository.findByEmail(employerEmail)
                .orElseThrow(()->new RuntimeException("Employer not found"));

        if(employer.getCompany()==null)
        {
            throw new RuntimeException("Employer does not have company");
        }

        Job job=mapJobDtoToJob(jobDto);
        job.setPostedDate(Instant.now());
        job.setApplicationsCount(0);
        job.setStatus("DRAFT");
        job.setCompany(employer.getCompany());
        Job savedJob=jobRepository.save(job);
        return ApplicationUtils.transferJobToDto(savedJob);
    }

    private Job mapJobDtoToJob(JobDto jobDto) {

        Job job=new Job();
        BeanUtils.copyProperties(jobDto,job);
        return job;
    }
}
