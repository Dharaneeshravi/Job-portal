package com.dharaneesh.job_portal_backend.auth;

import com.dharaneesh.job_portal_backend.dto.LoginRequestDto;
import com.dharaneesh.job_portal_backend.dto.LoginResponseDto;
import com.dharaneesh.job_portal_backend.security.util.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
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

              return ResponseEntity.ok(new LoginResponseDto(HttpStatus.OK.getReasonPhrase(),null,jwtToken));
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



}
