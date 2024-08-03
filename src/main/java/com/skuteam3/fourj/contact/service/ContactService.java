package com.skuteam3.fourj.contact.service;

import com.skuteam3.fourj.account.domain.User;
import com.skuteam3.fourj.account.domain.UserInfo;
import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.contact.domain.Contact;
import com.skuteam3.fourj.contact.dto.ContactRequestDto;
import com.skuteam3.fourj.contact.dto.ContactResponseDto;
import com.skuteam3.fourj.contact.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
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
    public ContactResponseDto createContact(ContactRequestDto contactRequestDto, String userEmail){
        User user = userRepository.findByEmail(userEmail).orElseThrow(()-> new IllegalArgumentException("user not found"));
        UserInfo userInfo = user.getUserInfo();

        Contact contact = new Contact();
        contact.setName(contactRequestDto.getName());
        contact.setNumber(contactRequestDto.getNumber());
        contact.setIsMain(contactRequestDto.getIsMain());
        contact.setUserInfo(userInfo);

        if (contactRequestDto.getIsMain()) {
            Optional<Contact> optionalIsMainContact = contactRepository.findContactByUserInfoAndIsMain(userInfo, true);
            if (optionalIsMainContact.isPresent()) {
                Contact isMainContact = optionalIsMainContact.get();
                isMainContact.setIsMain(false);

                contactRepository.save(isMainContact);
            }
        }

        return ContactResponseDto.from(contactRepository.save(contact));
    }

    @Transactional
    public Contact updateContact(Long id, ContactRequestDto contactRequestDto){
        Optional<Contact> contactOptional = contactRepository.findById(id);
        if(contactOptional.isPresent()){
            Contact contact = contactOptional.get();
            if(contactRequestDto.getName() != null){
                contact.setName(contactRequestDto.getName());
            }
            if(contactRequestDto.getNumber() != null){
                contact.setNumber(contactRequestDto.getNumber());
            }
            if(contactRequestDto.getIsMain() != null){
                if (contactRequestDto.getIsMain()) {

                    Optional<Contact> optionalIsMainContact = contactRepository.findContactByIdAndIsMain(id, true);
                    if (optionalIsMainContact.isPresent()) {

                        Contact isMainContact = optionalIsMainContact.get();
                        isMainContact.setIsMain(false);

                        contactRepository.save(isMainContact);
                    }
                }

                contact.setIsMain(contactRequestDto.getIsMain());
            }

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
