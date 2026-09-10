package com.dharaneesh.job_portal_backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ContactRequestDto(

        @NotBlank(message = "Email can not be empty")
        @Email(message = "Invalid email address")
        String email,

        @NotBlank(message = "Message can not be empty")
        @Size(min = 5,max = 500,message = "message should be between 5 and 500 characters")
        String message,

        @NotBlank(message = "Subject can not be empty")
        @Size(min = 3,max = 150,message = "Subject should be between 3 and 150 characters")
        String subject,

        @NotBlank(message = "Name can not be empty")
        @Size(min = 3,max = 30,message = "Name should be between 3 and 30 characters")
         String name,

        @NotBlank(message = "UserType can not be empty")
        @Pattern(regexp = "Job Seeker|Employer|Other", message = "UserType should be either 'Job Seeker' or 'Employer' or 'Other'")
        String userType) {
}
