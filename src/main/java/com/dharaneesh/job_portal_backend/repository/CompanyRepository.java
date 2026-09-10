package com.dharaneesh.job_portal_backend.repository;

import com.dharaneesh.job_portal_backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {
}
