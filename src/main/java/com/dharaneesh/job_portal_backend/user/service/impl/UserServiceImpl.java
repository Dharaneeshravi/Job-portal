package com.dharaneesh.job_portal_backend.user.service.impl;

import com.dharaneesh.job_portal_backend.constants.ApplicationConstants;
import com.dharaneesh.job_portal_backend.dto.*;
import com.dharaneesh.job_portal_backend.entity.*;
import com.dharaneesh.job_portal_backend.repository.*;
import com.dharaneesh.job_portal_backend.user.service.IUserService;
import com.dharaneesh.job_portal_backend.utils.ApplicationUtils;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements IUserService {

    private final JobPortalUserRepository jobPortalUserRepository;
    private final RoleRepository roleRepository;
    private final JobRepository jobRepository;
    private final CompanyRepository companyRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final ProfileRepository profileRepository;


    @Override
    public Optional<UserDto> searchUserByEmail(String email) {
        return jobPortalUserRepository.findByEmail(email)
                .map(this::mapToUserDto);
    }

    @Override
    @Transactional
    public UserDto elevateToEmployer(Long userId) {

        JobPortalUser user=jobPortalUserRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userId));

        if(ApplicationConstants.ROLE_EMPLOYER.equalsIgnoreCase(user.getRole().getName()))
        {
            return mapToUserDto(user);
        }


        if(ApplicationConstants.ROLE_ADMIN.equalsIgnoreCase(user.getRole().getName()))
        {
            throw new RuntimeException("cannot elevate admin user");
        }

        Role employerRole=roleRepository.findRoleByName(ApplicationConstants.ROLE_EMPLOYER)
                .orElseThrow(()->new RuntimeException("ROLE_EMPLOYER not found"));
        user.setRole(employerRole);

        JobPortalUser jobPortalUser=jobPortalUserRepository.save(user);
        return mapToUserDto(jobPortalUser);

    }

    @Override
    @Transactional
    public UserDto assignCompanyToEmployer(Long userId, Long companyId) {

        JobPortalUser user=jobPortalUserRepository.findById(userId)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userId));

        if(!ApplicationConstants.ROLE_EMPLOYER.equalsIgnoreCase(user.getRole().getName()))
        {
            throw new RuntimeException("cannot assign company to employer");
        }

        Company company=companyRepository.findById(companyId)
                .orElseThrow(()->new RuntimeException("Company not found"));
        user.setCompany(company);
        JobPortalUser jobPortalUser=jobPortalUserRepository.save(user);

        return mapToUserDto(jobPortalUser);
    }

    @Override
    @Transactional
    public ProfileDto createOrUpdateProfile(String userEmail, String profileJson, MultipartFile profilePicture, MultipartFile resume) throws JsonProcessingException {

        JobPortalUser user=jobPortalUserRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userEmail));

        Profile profile=user.getProfile();

        if(profile==null)
        {
            profile=new Profile();
            profile.setUser(user);
        }

        ObjectMapper objectMapper=new ObjectMapper();
        ProfileDto profileDto=objectMapper.readValue(profileJson, ProfileDto.class);
        Profile savedProfile=profileRepository.save(mapToProfile(profile,profileDto,profilePicture,resume));
        return mapToprofileDto(savedProfile,false);
    }

    @Override
    public ProfileDto getProfile(String userEmail) {

        JobPortalUser user=jobPortalUserRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userEmail));

        if(user.getProfile()==null)
        {
            return null;
        }

        return mapToprofileDto(user.getProfile(),false);
    }

    @Override
    public ProfileDto getProfilePicture(String userEmail) {

        JobPortalUser user=jobPortalUserRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userEmail));

        if(user.getProfile()==null)
        {
            return null;
        }
        return mapToprofileDto(user.getProfile(),true);
    }

    @Override
    public ProfileDto getResume(String userEmail) {

        JobPortalUser user=jobPortalUserRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userEmail));

        if(user.getProfile()==null)
        {
            return null;
        }
        return mapToprofileDto(user.getProfile(),true);
    }

    @Override
    @Transactional
    public JobDto saveJob(Long jobId, String userEmail) {

        JobPortalUser user=jobPortalUserRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userEmail));

        Job job=jobRepository.findById(jobId)
                .orElseThrow(()->new RuntimeException("Job not found ID: "+jobId));
        user.getSavedJobs().add(job);
        jobPortalUserRepository.save(user);
        return ApplicationUtils.transferJobToDto(job);

    }

    @Override
    @Transactional
    public void unsaveJob(Long jobId, String userEmail) {

        JobPortalUser user=jobPortalUserRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userEmail));

        Job job=jobRepository.findById(jobId)
                .orElseThrow(()->new RuntimeException("Job not found ID: "+jobId));

        user.getSavedJobs().remove(job);
        jobPortalUserRepository.save(user);

    }

    @Override
    public List<JobDto> getSavedJobs(String userEmail) {

        JobPortalUser user=jobPortalUserRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userEmail));

        return user.getSavedJobs().stream().map(ApplicationUtils::transferJobToDto).toList();
    }

    @Override
    @Transactional
    public JobApplicationDto applyForJob(String userEmail, ApplyJobRequestDto applyJobRequestDto) {

        JobPortalUser user=jobPortalUserRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userEmail));

        Long jobId=applyJobRequestDto.jobId();

        if(jobApplicationRepository.existsByUserIdAndJobId(user.getId(), jobId))
        {
            throw new RuntimeException("Job already exists");
        }

        Job job=jobRepository.findById(jobId)
                .orElseThrow(()->new RuntimeException("Job not found ID: "+jobId));


        JobApplication jobApplication=new JobApplication();
        jobApplication.setUser(user);
        jobApplication.setJob(job);
        jobApplication.setAppliedAt(Instant.now());
        jobApplication.setStatus(ApplicationConstants.STATUS_PENDING);
        jobApplication.setCoverLetter(applyJobRequestDto.coverLetter());
        JobApplication savedJobApplication=jobApplicationRepository.save(jobApplication);

        job.setApplicationsCount(job.getApplicationsCount()!=null?job.getApplicationsCount()+1:1);
        jobRepository.save(job);
        return ApplicationUtils.mapToJobApplicationDto(savedJobApplication);

    }

    @Override
    @Transactional
    public void withdrawApplication(String userEmail, Long jobId) {

        JobPortalUser user=jobPortalUserRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userEmail));

          if(!jobApplicationRepository.existsByUserIdAndJobId(user.getId(), jobId))
          {
              throw new RuntimeException("You have not enough job applications to apply for this application");
          }
          jobApplicationRepository.deleteByUserIdAndJobId(user.getId(), jobId);

        Job job=jobRepository.findById(jobId)
                .orElseThrow(()->new RuntimeException("Job not found ID: "+jobId));

        if(job.getApplicationsCount()!=null && job.getApplicationsCount()>0)
        {
               job.setApplicationsCount(job.getApplicationsCount()-1);
        }
        jobRepository.save(job);

    }

    @Override
    public List<JobApplicationDto> getJobSeekerApplication(String userEmail) {

        JobPortalUser user=jobPortalUserRepository.findByEmail(userEmail)
                .orElseThrow(()->new RuntimeException("User not found ID: "+userEmail));

        return user.getJobApplications().stream().map(ApplicationUtils::mapToJobApplicationDto).toList();
    }


    private ProfileDto mapToprofileDto(Profile profile, boolean includeBinaryData) {
        ProfileDto dto;
        if (includeBinaryData) {
            dto = new ProfileDto(profile.getId(), profile.getUser().getId(),
                    profile.getJobTitle(), profile.getLocation(), profile.getExperienceLevel(),
                    profile.getProfessionalBio(), profile.getPortfolioWebsite(), profile.getProfilePicture(),
                    profile.getProfilePictureName(), profile.getProfilePictureType(), profile.getResume(),
                    profile.getResumeName(), profile.getResumeType(), profile.getCreatedAt(), profile.getUpdatedAt()
            );
        } else {
            dto = new ProfileDto(profile.getId(), profile.getUser().getId(),
                    profile.getJobTitle(), profile.getLocation(), profile.getExperienceLevel(),
                    profile.getProfessionalBio(), profile.getPortfolioWebsite(), null,
                    profile.getProfilePictureName(), profile.getProfilePictureType(), null,
                    profile.getResumeName(), profile.getResumeType(), profile.getCreatedAt(), profile.getUpdatedAt());
        }
        return dto;
    }

    private Profile mapToProfile(Profile profile, ProfileDto profileDto, MultipartFile profilePicture, MultipartFile resume) {

        profile.setJobTitle(profileDto.jobTitle());
        profile.setLocation(profileDto.location());
        profile.setExperienceLevel(profileDto.experienceLevel());
        profile.setProfessionalBio(profileDto.professionalBio());
        profile.setPortfolioWebsite(profileDto.portfolioWebsite());
        // Handle profile picture upload
        if (profilePicture != null && !profilePicture.isEmpty()) {
            try {
                profile.setProfilePicture(profilePicture.getBytes());
                profile.setProfilePictureName(profilePicture.getOriginalFilename());
                profile.setProfilePictureType(profilePicture.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload profile picture", e);
            }
        }
        // Handle resume upload
        if (resume != null && !resume.isEmpty()) {
            try {
                profile.setResume(resume.getBytes());
                profile.setResumeName(resume.getOriginalFilename());
                profile.setResumeType(resume.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Failed to upload resume", e);
            }
        }
        return profile;
    }

    private UserDto mapToUserDto(JobPortalUser user) {

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto);
        userDto.setUserId(user.getId());
        userDto.setRole(user.getRole()!=null?user.getRole().getName():null);
        userDto.setCompanyId(user.getCompany()!=null?user.getCompany().getId():null);
        userDto.setCompanyName(user.getCompany()!=null?user.getCompany().getName():null);
        return userDto;
    }
}
