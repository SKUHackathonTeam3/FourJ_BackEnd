package com.skuteam3.fourj.contact.controller;

import com.skuteam3.fourj.account.repository.UserRepository;
import com.skuteam3.fourj.contact.domain.Contact;
import com.skuteam3.fourj.contact.dto.ContactDto;
import com.skuteam3.fourj.contact.service.ContactService;
import com.skuteam3.fourj.jwt.provider.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@RequestMapping("/api/v1/contact")
@RestController
public class ContactController {

    private final JwtProvider jwtProvider;
    private final ContactService contactService;

    //Create
    @PostMapping
    public String createContact(@RequestBody ContactDto contactDto, HttpServletRequest request){
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        contactService.createContact(contactDto, userEmail);
        return "Contact created successfully";
    }

    //Read
    @GetMapping
    public ResponseEntity<List<ContactDto>> getContactByUserEmail(HttpServletRequest request){
        String userEmail = jwtProvider.validate(jwtProvider.parseToken(request));

        List<Contact> contacts = contactService.getContactByUserEmail(userEmail);
        List<ContactDto> contactDtos = new ArrayList<>();
        for (Contact contact : contacts) {
            ContactDto dto = ContactDto.from(contact);
            contactDtos.add(dto);
        }
        return ResponseEntity.ok(contactDtos);
    }

    //Update
    @PatchMapping("/{id}")
    public ResponseEntity<ContactDto> updateContact(@PathVariable Long id, @RequestBody ContactDto contactDto){
        try{
            Contact updateContact = contactService.updateContact(id, contactDto);
            return ResponseEntity.ok(ContactDto.from(updateContact));
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id){
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

}
