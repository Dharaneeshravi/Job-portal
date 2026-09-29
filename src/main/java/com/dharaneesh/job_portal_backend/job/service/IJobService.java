package com.dharaneesh.job_portal_backend.job.service;

import com.dharaneesh.job_portal_backend.dto.JobApplicationDto;
import com.dharaneesh.job_portal_backend.dto.JobDto;
import com.dharaneesh.job_portal_backend.dto.UpdateJobApplicationDto;
import jakarta.validation.Valid;

import java.util.List;

public interface IJobService {
    List<JobDto> getEmployerJobs(String employerEmail);

    JobDto updateJobStatus(String jobId, String status, String employerEmail);

    JobDto createJob(@Valid JobDto jobDto, String employerEmail);

    List<JobApplicationDto> getApplicationByJobForEmployer(String jobId);

    boolean updateJobApplication(@Valid UpdateJobApplicationDto updateJobApplicationDto);
}
