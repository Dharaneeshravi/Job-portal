package com.dharaneesh.job_portal_backend.utils;

import com.dharaneesh.job_portal_backend.constants.ApplicationConstants;
import com.dharaneesh.job_portal_backend.entity.JobPortalUser;
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
}
