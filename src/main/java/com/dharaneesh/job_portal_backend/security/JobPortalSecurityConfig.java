package com.dharaneesh.job_portal_backend.security;

import com.dharaneesh.job_portal_backend.security.util.JwtTokenValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JobPortalSecurityConfig {

    @Qualifier("publicpaths")
    private final List<String> publicPaths;

    @Qualifier("securedPaths")
    private final List<String> privatePaths;

    @Qualifier("adminpaths")
    private final List<String> adminpaths;

    private final AuthenticationProvider authenticationProvider;


    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {

             return  http
//                     .csrf(csrfConfif->csrfConfif
//                             .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                             .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
//                     )
                     .csrf(AbstractHttpConfigurer::disable)

                     .authorizeHttpRequests(request->{

                         publicPaths.forEach(path-> request.requestMatchers(path).permitAll());
                         privatePaths.forEach(path->request.requestMatchers(path).authenticated());
                         adminpaths.forEach(path->request.requestMatchers(path).hasRole("ADMIN"));
                         request.anyRequest().denyAll();
                     })
                       .addFilterBefore(new JwtTokenValidatorFilter(publicPaths), BasicAuthenticationFilter.class)
                       .cors(Customizer.withDefaults())
                       .formLogin(AbstractHttpConfigurer::disable)
                       .httpBasic(Customizer.withDefaults()).build();

    }



    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationProvider authenticationProvider) {
       return new ProviderManager(authenticationProvider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    private CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
