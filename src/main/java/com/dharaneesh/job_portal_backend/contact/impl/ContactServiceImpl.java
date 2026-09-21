package com.dharaneesh.job_portal_backend.contact.impl;

import com.dharaneesh.job_portal_backend.constants.ApplicationConstants;
import com.dharaneesh.job_portal_backend.contact.IContactService;
import com.dharaneesh.job_portal_backend.dto.ContactRequestDto;
import com.dharaneesh.job_portal_backend.dto.ContactResponseDto;
import com.dharaneesh.job_portal_backend.entity.Contact;
import com.dharaneesh.job_portal_backend.repository.ContactRepository;
import com.dharaneesh.job_portal_backend.utils.ApplicationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;


    @Override
    @Transactional
    public boolean saveContact(ContactRequestDto contactRequestDto) {

       Contact contact=contactRepository.save(transformToEntity(contactRequestDto));
        return contact.getId() != null;
    }

    @Override
    public List<ContactResponseDto> fetchNewContacts() {

        List<Contact> contactList= contactRepository.findByStatus(ApplicationConstants.NEW_MESSAGE);

         return contactList.stream().map(this::mapToContactResponseDto).toList();
    }

    @Override
    public List<ContactResponseDto> fetchNewContactsBySorted(String sortBy, String sortOrder) {


        Sort sort=sortOrder.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending()
                :Sort.by(sortBy).ascending();

        List<Contact> contactList= contactRepository.findByStatus(ApplicationConstants.NEW_MESSAGE,sort);
        return contactList.stream().map(this::mapToContactResponseDto).toList();
    }

    @Override
    public Page<ContactResponseDto> fetchNewContactsWithPageAndSorted(int pageNumber, int pageSize, String sortBy, String sortOrder) {

        Sort sort=sortOrder.equalsIgnoreCase("desc")?
                Sort.by(sortBy).descending()
                :Sort.by(sortBy).ascending();

        Pageable pageable= PageRequest.of(pageNumber,pageSize,sort);

        Page<Contact> contactPage=contactRepository.findByStatus(ApplicationConstants.NEW_MESSAGE,pageable);
        return contactPage.map(this::mapToContactResponseDto);
    }

    @Override
    @Transactional
    public boolean closeContactMsg(Long id, String status) {

        int updatedRows=contactRepository.updateStatusById(status,id, ApplicationUtils.getLoggedInUser());
        return updatedRows>0;
    }


    private ContactResponseDto mapToContactResponseDto(Contact contact)
    {

        return new ContactResponseDto(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                contact.getUserType(),
                contact.getSubject(),
                contact.getMessage(),
                contact.getStatus(),
                contact.getCreatedAt()
        );
    }

    private Contact transformToEntity(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto,contact);
        contact.setStatus("NEW");
        return contact;
    }
}
