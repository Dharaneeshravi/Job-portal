package com.dharaneesh.job_portal_backend.constants;

public class ApplicationConstants {

    private  ApplicationConstants() {
        throw new IllegalStateException("Utility class");
    }

    public static final String JWT_SECRET_KEY = "JWT_SECRET_KEY";
    public static final String JWT_SECRET_DEFAULT_VALUE= "7f3a9c2e8d1b6a4f0c5e9d2b7a3f8c1e";
    public static final String JWT_HEADER = "Authorization";
    public static final String ROLE_JOB_SEEKER = "ROLE_JOB_SEEKER";
    public static final String NEW_MESSAGE = "NEW";
    public static final String CLOSE_MESSAGE = "CLOSE_MESSAGE";
    public  static final String SYSTEM = "SYSTEM";
    public  static final String ROLE_ADMIN = "ROLE_ADMIN";
    public  static final String ROLE_EMPLOYER = "ROLE_EMPLOYER";
    public  static final String STATUS_PENDING= "PENDING";
}
