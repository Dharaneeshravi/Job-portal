package com.dharaneesh.job_portal_backend.dto;


import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public record CompanyDto(Long id, String name, String logo, String industry,
                         String size , BigDecimal rating, String locations, int founded,
                         String description, int employees, String website, Instant createdAt, List<JobDto> jobList) {
}
