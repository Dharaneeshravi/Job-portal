package com.dharaneesh.job_portal_backend.entity;

import com.dharaneesh.job_portal_backend.dto.ProfileDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter @Setter
public class JobPortalUser extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password_hash", nullable = false, length = 500)
    private String passwordHash;

    @Column(name = "mobile_number", length = 20)
    private String mobileNumber;

    @ManyToOne(fetch = FetchType.EAGER,optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @ManyToOne(fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.SET_NULL)
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(mappedBy = "user")
    private Profile profile;

    @ManyToMany
    @JoinTable(
            name = "saved_jobs",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "job_id")
    )
    private Set<Job> savedJobs=new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<JobApplication> jobApplications=new LinkedHashSet<>();


}
