package com.dharaneesh.job_portal_backend.job.controller;

import com.dharaneesh.job_portal_backend.job.service.IJobService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
