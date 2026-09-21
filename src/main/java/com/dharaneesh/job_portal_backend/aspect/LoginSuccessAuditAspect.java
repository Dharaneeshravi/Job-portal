package com.dharaneesh.job_portal_backend.aspect;

import com.dharaneesh.job_portal_backend.dto.LoginResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class LoginSuccessAuditAspect {


    @AfterReturning(
            pointcut = "execution(* com.dharaneesh.job_portal_backend.auth.AuthController.apiLogin(..))",
            returning = "response"
    )
    public void logSuccessFullLogin(JoinPoint joinPoint, Object response)
    throws Throwable
    {
        if(!(response instanceof ResponseEntity<?> responseEntity))
        {
            return;
        }

        Object body = responseEntity.getBody();

        if(!(body instanceof LoginResponseDto loginResponseDto))
        {
            return;
        }

        if(loginResponseDto.userDto()!=null)
        {
             String userName=loginResponseDto.userDto().getName();
             String role=loginResponseDto.userDto().getRole();
             log.info("user logged in successfully for {} {}",userName,role);
        }
    }
}
