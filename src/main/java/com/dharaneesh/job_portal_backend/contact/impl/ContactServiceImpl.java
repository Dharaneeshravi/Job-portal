package com.dharaneesh.job_portal_backend.contact.impl;

import com.dharaneesh.job_portal_backend.contact.IContactService;
import com.dharaneesh.job_portal_backend.dto.ContactRequestDto;
import com.dharaneesh.job_portal_backend.entity.Contact;
import com.dharaneesh.job_portal_backend.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements IContactService {

    private final ContactRepository contactRepository;


    @Override
    public boolean saveContact(ContactRequestDto contactRequestDto) {

       Contact contact=contactRepository.save(transformToEntity(contactRequestDto));
        return contact.getId() != null;
    }

    private Contact transformToEntity(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto,contact);
        contact.setStatus("NEW");
        return contact;
    }
}
