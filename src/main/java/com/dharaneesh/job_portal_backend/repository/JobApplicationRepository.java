package com.dharaneesh.job_portal_backend.repository;

import com.dharaneesh.job_portal_backend.dto.JobApplicationDto;
import com.dharaneesh.job_portal_backend.entity.JobApplication;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication,Long> {

    boolean existsByUserIdAndJobId(Long userId, Long jobId);

    void deleteByUserIdAndJobId(Long userId, Long jobId);

    List<JobApplication> findByUserIdOrderByAppliedAtDesc(Long userId);

    List<JobApplication> findByJobIdOrderByAppliedAtAsc(String jobId);

    @Query(" UPDATE JobApplication j SET j.status=:status , j.notes:notes,j.updatedAt=CURRENT_TIMESTAMP,j.updatedBy=:loggedInUser WHERE j.id=:id")
    int updateStatusAndNotesById(String status, String notes, Long id, String loggedInUser);
}
