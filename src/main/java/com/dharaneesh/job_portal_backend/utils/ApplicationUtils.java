package com.dharaneesh.job_portal_backend.utils;

import com.dharaneesh.job_portal_backend.constants.ApplicationConstants;
import com.dharaneesh.job_portal_backend.dto.JobApplicationDto;
import com.dharaneesh.job_portal_backend.dto.JobDto;
import com.dharaneesh.job_portal_backend.dto.ProfileDto;
import com.dharaneesh.job_portal_backend.entity.Job;
import com.dharaneesh.job_portal_backend.entity.JobApplication;
import com.dharaneesh.job_portal_backend.entity.JobPortalUser;
import com.dharaneesh.job_portal_backend.entity.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class ApplicationUtils {


    public static  String getLoggedInUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || !authentication.isAuthenticated()|| authentication.getPrincipal().equals("anonymousUser")){

            return ApplicationConstants.SYSTEM;
        }

        Object principal = authentication.getPrincipal();
        String userName;
        if(principal instanceof JobPortalUser)
        {
            userName=((JobPortalUser) principal).getEmail();
        }else {
            userName=principal.toString();
        }
        return userName;

    }


    public static JobDto transferJobToDto(Job job) {

        return new JobDto(
                job.getId(),
                job.getTitle(),
                job.getCompany().getId(),
                job.getCompany().getName(),
                job.getCompany().getLogo(),
                job.getLocation(),
                job.getWorkType(),
                job.getJobType(),
                job.getCategory(),
                job.getExperienceLevel(),
                job.getSalaryMin(),
                job.getSalaryMax(),
                job.getSalaryCurrency(),
                job.getSalaryPeriod(),
                job.getDescription(),
                job.getRequirements(),
                job.getBenefits(),
                job.getPostedDate(),
                job.getApplicationDeadline(),
                job.getApplicationsCount(),
                job.getFeatured(),
                job.getUrgent(),
                job.getRemote(),
                job.getStatus()
        );
    }


    public static JobApplicationDto mapToJobApplicationDto(JobApplication application) {

        ProfileDto profileDto = null;
        Profile profile = application.getUser().getProfile();
        if (profile != null) {
            profileDto = new ProfileDto(
                    profile.getId(),
                    profile.getUser().getId(),
                    profile.getJobTitle(),
                    profile.getLocation(),
                    profile.getExperienceLevel(),
                    profile.getProfessionalBio(),
                    profile.getPortfolioWebsite(),
                    profile.getProfilePicture(),
                    profile.getProfilePictureName(),
                    profile.getProfilePictureType(),
                    profile.getResume(),
                    profile.getResumeName(),
                    profile.getResumeType(),
                    profile.getCreatedAt(),
                    profile.getUpdatedAt()
            );
        }
        return new JobApplicationDto(
                application.getId(),
                application.getUser().getId(),
                application.getUser().getName(),
                application.getUser().getEmail(),
                application.getUser().getMobileNumber(),
                profileDto,
                ApplicationUtils.transferJobToDto(application.getJob()),
                application.getAppliedAt(),
                application.getStatus(),
                application.getCoverLetter(),
                application.getNotes()
        );
    }

}
