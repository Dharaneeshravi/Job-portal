package com.dharaneesh.job_portal_backend.contact.controller;

import com.dharaneesh.job_portal_backend.contact.IContactService;
import com.dharaneesh.job_portal_backend.dto.ContactRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
@Validated
public class ContactController {

    private final IContactService contactService;


    @PostMapping(version = "1.0")
    public ResponseEntity<String> saveContactMsg(@RequestBody @Valid ContactRequestDto contactRequestDto)
    {
        boolean isSaved = contactService.saveContact(contactRequestDto);

        if(isSaved)
        {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Contact saved successfully");
        }else
        {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Request processing failed");
        }
    }


}
