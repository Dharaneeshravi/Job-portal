package com.dharaneesh.job_portal_backend.repository;

import com.dharaneesh.job_portal_backend.entity.Company;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface CompanyRepository extends JpaRepository<Company,Long> {

    @Cacheable("jobs")
    @Query("SELECT DISTINCT c FROM Company c JOIN FETCH c.jobs j WHERE j.status=:status")
    List<Company> findAllWithJobStatus(@Param("status") String status);

    @CacheEvict(value = "companies", allEntries = true)
    void deleteById(Long id);

    @CacheEvict(key = "companies", allEntries = true)
    Company save(Company company);

    @CacheEvict(value = "companies",allEntries = true)
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Company c SET c.name=:name,c.logo=:logo,c.industry=:industry,c.size=:size,c.rating=:rating,c.locations=:locations,c.founded=:founded,c.description=:description,c.employees=:employees,c.website=:website WHERE c.id=:id")
    int updateCompanyDetails(
            @Param("id") Long id,
            @Param("name") String name,
            @Param("logo")String logo,
            @Param("industry")String industry,
            @Param("size")String size,
            @Param("rating")BigDecimal rating,
            @Param("locations")String locations,
            @Param("founded")Integer founded,
            @Param("description")String description,
            @Param("employees")Integer employees,
            @Param("website")String website);
}
