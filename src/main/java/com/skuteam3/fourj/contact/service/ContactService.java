package com.skuteam3.fourj.contact.service;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.contact.domain.Contact;
import com.skuteam3.fourj.contact.dto.ContactDto;
import com.skuteam3.fourj.contact.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Optionals;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class ContactService {
    private final ContactRepository contactRepository;
    private final UserRepository userRepository;

    @Transactional
    public Contact createContact(ContactDto contactDto, String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        Contact contact = new Contact();
        contact.setName(contactDto.getName());
        contact.setNumber(contactDto.getNumber());
        contact.setIsMain(contactDto.getIsMain());
        contact.setUserInfo(userInfo);

        return contactRepository.save(contact);
    }

    @Transactional
    public Contact updateContact(Long id, ContactDto contactDto){
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if(contactOptional.isPresent()){
            Contact contact = contactOptional.get();
            contact.setName(contactDto.getName());
            contact.setNumber(contactDto.getNumber());
            contact.setIsMain(contactDto.getIsMain());
            return contactRepository.save(contact);
        }else {
            throw new RuntimeException("Contact not found with id" + id);
        }
    }

    @Transactional
    public void deleteContact(Long id){
        contactRepository.deleteById(id);
    }

    public List<Contact> getContactByUserEmail(String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        return contactRepository.findContactByUserInfo(userInfo);
    }
}
