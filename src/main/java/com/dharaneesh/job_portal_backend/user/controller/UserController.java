package com.dharaneesh.job_portal_backend.user.controller;

import com.dharaneesh.job_portal_backend.dto.UserDto;
import com.dharaneesh.job_portal_backend.user.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;


    @GetMapping(path = "/search/admin")
    public ResponseEntity<?> searchUserByEmail(@RequestParam("email") String email) {

        Optional<UserDto> userDtoOptional=userService.searchUserByEmail(email);

        if(userDtoOptional.isEmpty())
        {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("message", "User not found"));
        }
        return ResponseEntity.ok(userDtoOptional.get());
    }

    @PatchMapping(path = "/{userId}/role/employer/admin")
    public ResponseEntity<?> elevateToEmployer(@PathVariable("userId") Long userId) {

        UserDto updatedUser=userService.elevateToEmployer(userId);
        return ResponseEntity.ok(updatedUser);
    }

    @PatchMapping(path = "/{userId}/company/{companyId}/admin")
    public ResponseEntity<?> assignCompanyToEmployer(@PathVariable("userId") Long userId, @PathVariable("companyId") Long companyId) {

        return ResponseEntity.ok(userService.assignCompanyToEmployer(userId,companyId));
    }
}
