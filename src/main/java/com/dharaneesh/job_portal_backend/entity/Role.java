package com.dharaneesh.job_portal_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter @Setter
public class Role extends BaseEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME",nullable = false,unique = true)
    private String name;

//    @OneToMany(mappedBy = "role")
//    private List<JobPortalUser> jobPortalUsers;
}
