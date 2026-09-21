package com.dharaneesh.job_portal_backend.contact;

import com.dharaneesh.job_portal_backend.dto.ContactRequestDto;
import com.dharaneesh.job_portal_backend.dto.ContactResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface IContactService {
    boolean saveContact(ContactRequestDto contactRequestDto);

    List<ContactResponseDto> fetchNewContacts();

    List<ContactResponseDto> fetchNewContactsBySorted(String sortBy, String sortOrder);

    Page<ContactResponseDto> fetchNewContactsWithPageAndSorted(int pageNumber, int pageSize, String sortBy, String sortOrder);

    boolean closeContactMsg(Long id, String status);
}
