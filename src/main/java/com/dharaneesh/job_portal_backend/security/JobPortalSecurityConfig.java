package com.dharaneesh.job_portal_backend.security;

import com.dharaneesh.job_portal_backend.security.util.JwtTokenValidatorFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class JobPortalSecurityConfig {

    @Qualifier("publicpaths")
    private final List<String> publicPaths;

    @Qualifier("securedPaths")
    private final List<String> privatePaths;


    @Bean
    SecurityFilterChain customSecurityFilterChain(HttpSecurity http) {

             return  http
                     .authorizeHttpRequests(request->{

                         publicPaths.forEach(path-> request.requestMatchers(path).permitAll());
                         privatePaths.forEach(path->request.requestMatchers(path).authenticated());
                         request.anyRequest().denyAll();
                     })
                       .addFilterBefore(new JwtTokenValidatorFilter(publicPaths), BasicAuthenticationFilter.class)
                       .cors(Customizer.withDefaults())
                       .csrf(AbstractHttpConfigurer::disable)
                       .formLogin(AbstractHttpConfigurer::disable)
                       .httpBasic(Customizer.withDefaults()).build();

    }


    @Bean
    public UserDetailsService userDetailsService() {

       var admin= User.builder().username("admin")
                .password(passwordEncoder().encode("admin")).roles("ADMIN").build();

       var user= User.builder().username("user").password(passwordEncoder().encode("user")).roles("USER").build();

       return new InMemoryUserDetailsManager(admin,user);
    }

    @Bean
    public AuthenticationManager authenticationManagerBean() {

        var authentication=new DaoAuthenticationProvider(userDetailsService());
        authentication.setPasswordEncoder(passwordEncoder());
       return new ProviderManager(authentication);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
