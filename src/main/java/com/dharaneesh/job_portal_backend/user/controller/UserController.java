package com.dharaneesh.job_portal_backend.user.controller;

import com.dharaneesh.job_portal_backend.dto.*;
import com.dharaneesh.job_portal_backend.user.service.IUserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;


    @GetMapping(path = "/search/admin")
    public ResponseEntity<?> searchUserByEmail(@RequestParam("email") String email) {

        Optional<UserDto> userDtoOptional = userService.searchUserByEmail(email);

        if (userDtoOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }
        return ResponseEntity.ok(userDtoOptional.get());
    }

    @PatchMapping(path = "/{userId}/role/employer/admin")
    public ResponseEntity<?> elevateToEmployer(@PathVariable("userId") Long userId) {

        UserDto updatedUser = userService.elevateToEmployer(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(path = "/{userId}/company/{companyId}/admin")
    public ResponseEntity<?> assignCompanyToEmployer(@PathVariable("userId") Long userId, @PathVariable("companyId") Long companyId) {

        return ResponseEntity.ok(userService.assignCompanyToEmployer(userId, companyId));
    }

    @PutMapping(path = "/profile/jobseeker", version = "1.0", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProfileDto> createOrUpdateProfile(
            @RequestPart(value = "profile") String profileJson,
            @RequestPart(value = "profilePicture", required = false) MultipartFile profilePicture,
            @RequestPart(value = "resume", required = false) MultipartFile resume,
            Authentication authentication
    ) throws JsonProcessingException {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(userService.createOrUpdateProfile(userEmail, profileJson, profilePicture, resume));
    }

    @GetMapping(path = "/profile/jobseeker", version = "1.0")
    public ResponseEntity<ProfileDto> getProfile(Authentication authentication) {

        String userEmail = authentication.getName();
        return ResponseEntity.ok(userService.getProfile(userEmail));
    }

    @GetMapping(path = "/profile/picture/jobseeker", version = "1.0")
    public ResponseEntity<byte[]> getProfilePicture(Authentication authentication) {

        String userEmail = authentication.getName();

        ProfileDto profileDto = userService.getProfilePicture(userEmail);

        byte[] picture = profileDto.profilePicture();

        if (picture == null || picture.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profileDto.profilePictureType()));
        headers.setContentLength(picture.length);
        return new ResponseEntity<>(picture, headers, HttpStatus.OK);


    }

    @GetMapping(path = "/profile/resume/jobseeker")
    public ResponseEntity<byte[]> getResume(Authentication authentication) {
        String userEmail = authentication.getName();

        ProfileDto profileDto = userService.getResume(userEmail);

        byte[] resume = profileDto.resume();

        if (resume == null || resume.length == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(profileDto.resumeType()));
        headers.setContentLength(resume.length);
        headers.setContentDispositionFormData("attachment", profileDto.resumeName());
        return new ResponseEntity<>(resume, headers, HttpStatus.OK);
    }

    @PostMapping(path = "/saved-jobs/{jobId}/jobseeker", version = "1.0")
    public ResponseEntity<JobDto> saveJob(@PathVariable Long jobId, Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userService.saveJob(jobId, userEmail));
    }

    @DeleteMapping(path = "/saved-jobs/{jobId}/jobseeker", version = "1.0")
    public ResponseEntity<String> unsaveJob(@PathVariable Long jobId, Authentication authentication) {
        String userEmail = authentication.getName();
        userService.unsaveJob(jobId, userEmail);
        return ResponseEntity.ok("Job Unsaved Successfully");
    }

    @GetMapping(path = "/saved-job/jobseeker", version = "1.0")
    public ResponseEntity<List<JobDto>> getSavedJobs(Authentication authentication) {
        String userEmail = authentication.getName();

        return ResponseEntity.ok(userService.getSavedJobs(userEmail));
    }

    @PostMapping(path = "/job-application/jobseeker",version = "1.0")
    public ResponseEntity<JobApplicationDto> applyForJob(@RequestBody @Valid ApplyJobRequestDto applyJobRequestDto, Authentication authentication) {

        String userEmail = authentication.getName();
        return ResponseEntity.status(HttpStatus.CREATED).body(userService.applyForJob(userEmail,applyJobRequestDto));
    }

    @DeleteMapping(path = "/job-application/{jobId]/jobseeker")
    public ResponseEntity<String> withdrawApplication(@PathVariable Long jobId, Authentication authentication) {

        String userEmail = authentication.getName();
        userService.withdrawApplication(userEmail,jobId);
        return ResponseEntity.ok("Job Withdraw Successfully");
    }

    @GetMapping(path = "job-application/jobseeker",version = "1.0")
    public ResponseEntity<List<JobApplicationDto>> getJobSeekerApplication(Authentication authentication) {
        String userEmail = authentication.getName();
        return ResponseEntity.ok(userService.getJobSeekerApplication(userEmail));
    }
}
