package com.dharaneesh.job_portal_backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "COMPANIES")
@Getter @Setter
public class Company extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    private Long id;

    @Column(name = "NAME",nullable = false,unique = true)
    private String name;

    @Column(name = "LOGO",length = 500)
    private String logo;

    @Column(name = "INDUSTRY",nullable = false,length = 100)
    private String industry;

    @Column(name = "SIZE",length = 50,nullable = false)
    private String size;

    @Column(name = "RATING",nullable = false,precision = 3,scale = 2)
    private BigDecimal rating;

    @Column(name = "LOCATION",length = 1000)
    private String locations;

    @Column(name = "FOUNDED",nullable = false)
    private int founded;

    @Lob
    @Column(name = "DESCRIPTION")
    private String description;

    @Column(name = "EMPLOYEES")
    private int employees;

    @Column(name = "WEBSITE",length = 500)
    private String website;


    @OneToMany(mappedBy = "company",cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Job> jobs=new ArrayList<>();


}
