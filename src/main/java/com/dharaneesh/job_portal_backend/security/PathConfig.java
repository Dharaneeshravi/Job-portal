package com.dharaneesh.job_portal_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class PathConfig {

    @Bean(name = "publicpaths")
    public List<String> publicPath()
    {
        return List.of(
                "/api/register/public",
                "/api/auth/login/public",
                "/api/companies/public",
                "/api/contacts/public",
                "/api/swagger-ui.html",
                "/swagger-ui/**",
                "/api/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**",
                "/actuator/**"
        );
    }

    @Bean(name = "securedPaths")
    public List<String> securedPaths() {
        return List.of(
                "/api/**"
        );
    }

    @Bean(name = "employerpaths")
    public List<String> employerPaths() {
        return List.of(
                "/api/jobs/employer",
                "/api/jobs/${jobId}/status/employer"
        );
    }

    @Bean(name = "adminpaths")
    public List<String> adminPaths() {
        return List.of(
                "/api/contacts/admin",
                "/api/contacts/sort/admin",
                "/api/contacts/page/admin",
                "/api/contacts/${id}/status/admin",
                "/api/companies/admin",
                "/api/companies/${id}/admin"
        );
    }

    @Bean(name = "jobseekerPaths")
    public List<String> jobseekerPaths() {
        return List.of(
                "/api/users/profile/jobseeker",
                "/api/users/profile/picture/jobseeker",
                "/api/users/profile/resume/jobseeker",
                "/api/users/saved-jobs/${jobId}/jobseeker",
                "/api/users/saved-jobs/jobseeker",
                "/api/users/job-applications/jobseeker",
                "/api/users/job-applications/${jobId}/jobseeker"
        );
    }
}
