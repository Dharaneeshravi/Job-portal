package com.dharaneesh.job_portal_backend.security.util;

import com.dharaneesh.job_portal_backend.constants.ApplicationConstants;
import com.dharaneesh.job_portal_backend.entity.JobPortalUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@PropertySource(value = "classpath:jwt.properties")
public class JwtUtils {

    private final Environment environment;

    @Value("${jwt.issuer:Job Portal}")
    private String jwtIssuer;

    @Value("${jwt.subject:JWT Token}")
    private String jwtSubject;

    @Value("${jwt.expiration.hours:1}")
    private int jwtExpirationHours;

    @Value("${jwt.prod.expiration.hours:1}")
    private int jwtProdExpirationHours;


    public String generateToken(Authentication authentication) {

        String secret = environment.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);

        SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

        JobPortalUser fetchUser= (JobPortalUser) authentication.getPrincipal();

       return  Jwts.builder().issuer(jwtIssuer).subject(jwtSubject)
                .claim("name",fetchUser.getName())
                .claim("email",fetchUser.getEmail())
                .claim("mobileNumber",fetchUser.getMobileNumber())
                .claim("role",authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(",")))
                .issuedAt(new Date())
                .expiration(new Date((new Date()).getTime()+jwtExpirationHours*60*60*1000))
                .signWith(secretKey).compact();
    }
}
