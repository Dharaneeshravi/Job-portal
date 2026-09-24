package com.dharaneesh.job_portal_backend.user.service.impl;

import com.dharaneesh.job_portal_backend.constants.ApplicationConstants;
import com.dharaneesh.job_portal_backend.dto.UserDto;
import com.dharaneesh.job_portal_backend.entity.Company;
import com.dharaneesh.job_portal_backend.entity.JobPortalUser;
import com.dharaneesh.job_portal_backend.entity.Role;
import com.dharaneesh.job_portal_backend.repository.CompanyRepository;
import com.dharaneesh.job_portal_backend.repository.JobPortalUserRepository;
import com.dharaneesh.job_portal_backend.repository.RoleRepository;
import com.dharaneesh.job_portal_backend.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final RoleRepository roleRepository;
    private final CompanyRepository companyRepository;


    @Override
    public Optional<UserDto> searchUserByEmail(String email) {
        return jobPortalUserRepository.findByEmail(email)
                .map(this::mapToUserDto);
    }

    @Override
    @Transactional
    public UserDto elevateToEmployer(Long userId) {

        JobPortalUser user=jobPortalUserRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userId));

        if(ApplicationConstants.ROLE_EMPLOYER.equalsIgnoreCase(user.getRole().getName()))
        {
            return mapToUserDto(user);
        }


        if(ApplicationConstants.ROLE_ADMIN.equalsIgnoreCase(user.getRole().getName()))
        {
            throw new RuntimeException("cannot elevate admin user");
        }

        Role employerRole=roleRepository.findRoleByName(ApplicationConstants.ROLE_EMPLOYER)
                .orElseThrow(()->new RuntimeException("ROLE_EMPLOYER not found"));
        user.setRole(employerRole);

        JobPortalUser jobPortalUser=jobPortalUserRepository.save(user);
        return mapToUserDto(jobPortalUser);

    }

    @Override
    @Transactional
    public UserDto assignCompanyToEmployer(Long userId, Long companyId) {

        JobPortalUser user=jobPortalUserRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userId));

        if(!ApplicationConstants.ROLE_EMPLOYER.equalsIgnoreCase(user.getRole().getName()))
        {
            throw new RuntimeException("cannot assign company to employer");
        }

        Company company=companyRepository.findById(companyId)
                .orElseThrow(()->new RuntimeException("Company not found"));
        user.setCompany(company);
        JobPortalUser jobPortalUser=jobPortalUserRepository.save(user);

        return mapToUserDto(jobPortalUser);
    }

    private UserDto mapToUserDto(JobPortalUser user) {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setUserId(user.getId());
        userDto.setRole(user.getRole()!=null?user.getRole().getName():null);
        userDto.setCompanyId(user.getCompany()!=null?user.getCompany().getId():null);
        userDto.setCompanyName(user.getCompany()!=null?user.getCompany().getName():null);
        return userDto;
    }
}
