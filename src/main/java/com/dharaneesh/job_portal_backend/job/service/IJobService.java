package com.dharaneesh.job_portal_backend.job.service;

import com.dharaneesh.job_portal_backend.dto.JobDto;

import java.util.List;

public interface IJobService {
    List<JobDto> getEmployerJobs(String employerEmail);
}
