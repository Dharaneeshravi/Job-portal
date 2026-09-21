package com.dharaneesh.job_portal_backend.repository;

import com.dharaneesh.job_portal_backend.entity.Contact;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact,Long> {

    List<Contact> findByStatus(String aNew);
    List<Contact> findByStatus(String aNew, Sort sort);
    Page<Contact> findByStatus(String aNew, Pageable pageable);

    @Modifying(flushAutomatically = true,clearAutomatically = true)
    int updateStatusById(@Param("status") String status, @Param("id") Long id,@Param("updatedBy") String updatedBy);
}
