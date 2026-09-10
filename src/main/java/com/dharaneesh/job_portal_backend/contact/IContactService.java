package com.dharaneesh.job_portal_backend.contact;

import com.dharaneesh.job_portal_backend.dto.ContactRequestDto;

public interface IContactService {
    boolean saveContact(ContactRequestDto contactRequestDto);

}
