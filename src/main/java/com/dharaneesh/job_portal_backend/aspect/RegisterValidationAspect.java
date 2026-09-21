package com.dharaneesh.job_portal_backend.aspect;

import com.dharaneesh.job_portal_backend.dto.RegisterRequestDto;
import com.dharaneesh.job_portal_backend.entity.JobPortalUser;
import com.dharaneesh.job_portal_backend.exception.RegistrationValidationException;
import com.dharaneesh.job_portal_backend.repository.JobPortalUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.authentication.password.CompromisedPasswordDecision;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class RegisterValidationAspect {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final CompromisedPasswordChecker compromisedPasswordChecker;


    @Before(" execution(* com.dharaneesh.job_portal_backend.auth.AuthController.registerUser(..)) ")
    public void validateBeforeRegisterUser(JoinPoint joinPoint) throws Throwable
    {
          Object[] args = joinPoint.getArgs();
          RegisterRequestDto registerRequestDto = (RegisterRequestDto) args[0];

          log.info("Validate user request before registering");


        Map<String,String> error=new HashMap<>();


        CompromisedPasswordDecision decision=compromisedPasswordChecker
                .check(registerRequestDto.password());

        if(decision.isCompromised())
        {
            error.put("password","choose strong password");
        }

        Optional<JobPortalUser> existingUser=
                jobPortalUserRepository.findByEmailOrMobileNumber(registerRequestDto.email(),registerRequestDto.mobileNumber());


        if(existingUser.isPresent())
        {


            if(registerRequestDto.email().equalsIgnoreCase(existingUser.get().getEmail()))
            {
                error.put("email","email already exists");
            }

            if(registerRequestDto.mobileNumber().equalsIgnoreCase(existingUser.get().getMobileNumber()))
            {
                error.put("mobileNumber","mobile number already exists");
            }

        }

        if(!error.isEmpty())
        {
            log.error("Registering user failed {}",error);
            throw new RegistrationValidationException(error);
        }

        log.info("Registering user successfully");
    }

}
