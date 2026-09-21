package com.dharaneesh.job_portal_backend.auth;

import com.dharaneesh.job_portal_backend.constants.ApplicationConstants;
import com.dharaneesh.job_portal_backend.dto.LoginRequestDto;
import com.dharaneesh.job_portal_backend.dto.LoginResponseDto;
import com.dharaneesh.job_portal_backend.dto.RegisterRequestDto;
import com.dharaneesh.job_portal_backend.dto.UserDto;
import com.dharaneesh.job_portal_backend.entity.JobPortalUser;
import com.dharaneesh.job_portal_backend.entity.Role;
import com.dharaneesh.job_portal_backend.repository.JobPortalUserRepository;
import com.dharaneesh.job_portal_backend.repository.RoleRepository;
import com.dharaneesh.job_portal_backend.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JobPortalUserRepository jobPortalUserRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;


    @PostMapping(value = "/login/public",version = "1.0")
    public ResponseEntity<LoginResponseDto> apiLogin(@RequestBody LoginRequestDto loginRequestDto)
    {
          try {
              var authenticationResult=authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                      loginRequestDto.userName(),
                      loginRequestDto.password()
              ));

              String jwtToken=jwtUtils.generateToken(authenticationResult);

              UserDto userDto=new UserDto();
              var loggedInUser=(JobPortalUser)authenticationResult.getPrincipal();
              BeanUtils.copyProperties(loggedInUser,userDto);
              userDto.setUserId(loggedInUser.getId());
              userDto.setRole(loggedInUser.getRole().getName());
              return ResponseEntity.ok(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(),userDto,jwtToken));
          }
          catch (BadCredentialsException ex) {

              return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Invalid username and password");
          }
          catch (AuthenticationException ex) {
              return buildErrorResponse(HttpStatus.UNAUTHORIZED, "Authentication Failed");
          }
          catch (Exception ex) {
              return buildErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "An unexpected error has occurred");
          }
    }

    private ResponseEntity<LoginResponseDto> buildErrorResponse(HttpStatus status, String message)
    {
        return ResponseEntity
                .status(status)
                .body(new LoginResponseDto(message,null,null));
    }


    @PostMapping(path = "/register/public",version = "1.0")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequestDto registerRequestDto)
    {

        JobPortalUser jobPortalUser=new JobPortalUser();
        BeanUtils.copyProperties(registerRequestDto,jobPortalUser);
        jobPortalUser.setPasswordHash(passwordEncoder.encode(registerRequestDto.password()));
        Role role=roleRepository.findRoleByName(ApplicationConstants.ROLE_JOB_SEEKER)
                        .orElseThrow(()->new IllegalArgumentException("Role not found "+ApplicationConstants.ROLE_JOB_SEEKER));
        jobPortalUser.setRole(role);
        jobPortalUserRepository.save(jobPortalUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("success");
    }



}
