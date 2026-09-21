package com.dharaneesh.job_portal_backend.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;

@Configuration
public class PathConfig {

    @Bean(name = "publicpaths")
    public List<String> publicPath()
    {
        return List.of(
                "/api/register/public",
                "/api/auth/login/public",
                "/api/companies/public",
                "/api/contacts/public",
                "/api/swagger-ui.html",
                "/swagger-ui/**",
                "/api/v3/api-docs/**",
                "/swagger-resources/**",
                "/swagger-ui.html",
                "/webjars/**"
        );
    }

    @Bean(name = "securedPaths")
    public List<String> securedPaths() {
        return List.of(
                "/api/**"
        );
    }

    @Bean(name = "adminpaths")
    public List<String> adminPaths() {
        return List.of(
                "/api/contact/admin",
                "/api/contact/sort/admin",
                "/api/contact/${id}/status/admin"
        );
    }
}
