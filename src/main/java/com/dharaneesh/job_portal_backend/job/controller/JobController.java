package com.dharaneesh.job_portal_backend.job.controller;

import com.dharaneesh.job_portal_backend.dto.JobApplicationDto;
import com.dharaneesh.job_portal_backend.dto.JobDto;
import com.dharaneesh.job_portal_backend.dto.UpdateJobApplicationDto;
import com.dharaneesh.job_portal_backend.job.service.IJobService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jobs")
@RequiredArgsConstructor
public class JobController {

    private final IJobService jobService;

    @GetMapping(path = "/employer",version = "1.0")
    public ResponseEntity<?> getEmployerJobs(Authentication authentication) {

        String employerEmail=authentication.getName();
        return ResponseEntity.ok(jobService.getEmployerJobs(employerEmail));
    }

    @PatchMapping(path = "/{jobId}/status/employer",version = "1.0")
    public ResponseEntity<?> updateJobStatus(@PathVariable("jobId") String jobId, @RequestBody Map<String,String> requestBody, Authentication authentication) {
        String employerEmail=authentication.getName();
        String status=requestBody.get("status");

        if(status==null||status.trim().isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("status is empty");

        }
        return ResponseEntity.ok(jobService.updateJobStatus(jobId,status.toUpperCase(),employerEmail));
    }

    @PostMapping(path = "/employer",version = "1.0")
    public ResponseEntity<?> createJob(@RequestBody @Valid JobDto jobDto, Authentication authentication) {

        String employerEmail=authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(jobService.createJob(jobDto,employerEmail));
    }

    @GetMapping(path = "/application/{jobId}/employer")
    public ResponseEntity<List<JobApplicationDto>> getApplicationByJobForEmployer(@PathVariable("jobId") String jobId) {

        return ResponseEntity.ok(jobService.getApplicationByJobForEmployer(jobId));
    }

    @PatchMapping(path = "/application/employer")
    public ResponseEntity<String> updateJobApplication(@RequestBody @Valid UpdateJobApplicationDto updateJobApplicationDto) {

       boolean isUpdated= jobService.updateJobApplication(updateJobApplicationDto);

       if(isUpdated) {
           return ResponseEntity.ok("Job application updated successfully");
       }else
       {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Job application update failed");
       }
    }
}
