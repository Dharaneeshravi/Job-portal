package com.dharaneesh.job_portal_backend.security.util;

import com.dharaneesh.job_portal_backend.constants.ApplicationConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.env.Environment;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RequiredArgsConstructor
public class JwtTokenValidatorFilter extends OncePerRequestFilter {


    private final AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Qualifier("publicpaths")
    private final List<String> publicPath;

    
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

         String header=request.getHeader(ApplicationConstants.JWT_HEADER);

         if(header!=null && !header.isEmpty()) {

             try {
                 String jwt = header.substring(7);
                 Environment env = getEnvironment();

                 if (null != env) {
                     String secret = env.getProperty(ApplicationConstants.JWT_SECRET_KEY,
                             ApplicationConstants.JWT_SECRET_DEFAULT_VALUE);
                     SecretKey secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));

                     if (secretKey != null) {
                         Claims claims = Jwts.parser()
                                 .verifyWith(secretKey)
                                 .build()
                                 .parseSignedClaims(jwt)
                                 .getPayload();

                         String userName = String.valueOf(claims.get("email"));
                         String role = String.valueOf(claims.get("role"));

                         Authentication authentication = new UsernamePasswordAuthenticationToken(userName,
                                 null, AuthorityUtils.commaSeparatedStringToAuthorityList(role));
                         SecurityContextHolder.getContext().setAuthentication(authentication);
                     }

                 }

             } catch (Exception e) {

               throw new BadCredentialsException(e.getMessage());

             }

             filterChain.doFilter(request,response);

         }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {

        String path=request.getRequestURI();

        return publicPath.stream().anyMatch(publicPath->antPathMatcher
                .match(publicPath,path));


    }
}
