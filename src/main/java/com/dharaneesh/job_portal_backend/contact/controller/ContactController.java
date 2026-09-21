package com.dharaneesh.job_portal_backend.contact.controller;

import com.dharaneesh.job_portal_backend.constants.ApplicationConstants;
import com.dharaneesh.job_portal_backend.contact.IContactService;
import com.dharaneesh.job_portal_backend.dto.ContactRequestDto;
import com.dharaneesh.job_portal_backend.dto.ContactResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContacts()
    {
        return ResponseEntity.ok(contactService.fetchNewContacts());
    }


    @GetMapping("/sort/admin")
    public ResponseEntity<List<ContactResponseDto>> fetchNewContactsBySorted(
            @RequestParam(defaultValue = "createdAt",required = false) String sortBy,
            @RequestParam(defaultValue = "asc",required = false) String sortOrder
    )
    {
        return ResponseEntity.ok(contactService.fetchNewContactsBySorted(sortBy,sortOrder));
    }


    @GetMapping("/page/admin")
    public ResponseEntity<Page<ContactResponseDto>> fetchNewContactsWithPageAndSorted(
            @RequestParam(defaultValue = "0",required = false) int pageNumber,
            @RequestParam(defaultValue = "1",required = false) int pageSize,
            @RequestParam(defaultValue = "createdAt",required = false) String sortBy,
            @RequestParam(defaultValue = "asc",required = false) String sortOrder
    )
    {
        return ResponseEntity.ok(contactService.fetchNewContactsWithPageAndSorted(pageNumber,pageSize,sortBy,sortOrder));
    }

    @PatchMapping("/{id}/status/admin")
    public ResponseEntity<String> closeContactMsg(@PathVariable String id)
    {
        boolean isUpdated=contactService.closeContactMsg(Long.valueOf(id), ApplicationConstants.CLOSE_MESSAGE);

        if(isUpdated)
        {
            return ResponseEntity.ok("Contact message updated successfully");
        }
        else
        {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Failed to close contact message");
        }
    }





}
