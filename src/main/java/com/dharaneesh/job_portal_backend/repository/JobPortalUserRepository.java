package com.dharaneesh.job_portal_backend.repository;

import com.dharaneesh.job_portal_backend.entity.JobPortalUser;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobPortalUserRepository extends JpaRepository<JobPortalUser, Long> {
    Optional<JobPortalUser> findByEmailOrMobileNumber( String email, String mobileNumber);

    Optional<JobPortalUser> findByEmail(String username);
}
