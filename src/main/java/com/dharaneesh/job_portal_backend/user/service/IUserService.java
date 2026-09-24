package com.dharaneesh.job_portal_backend.user.service;

import com.dharaneesh.job_portal_backend.dto.UserDto;

import java.util.Optional;

public interface IUserService {
    Optional<UserDto> searchUserByEmail(String email);

    UserDto elevateToEmployer(Long userId);

    UserDto assignCompanyToEmployer(Long userId, Long companyId);
}
