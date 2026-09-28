package com.dharaneesh.job_portal_backend.repository;

import com.dharaneesh.job_portal_backend.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
}
