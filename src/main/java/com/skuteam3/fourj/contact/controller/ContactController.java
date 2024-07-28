package com.skuteam3.fourj.contact.controller;

import com.skuteam3.fourj.contact.domain.Contact;
import com.skuteam3.fourj.contact.dto.ContactRequestDto;
import com.skuteam3.fourj.contact.dto.ContactResponseDto;
import com.skuteam3.fourj.contact.service.ContactService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Tag(name = "contacts", description = "비상연락망 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/contacts")
@RestController
public class ContactController {

    private final ContactService contactService;

    //Create
    @Operation(
            summary = "비상연락망 생성",
            description = "로그인한 유저 정보에 맞는 비상연락망을 생성합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포와 contact를 연결하여 저장합니다."
    )
    @PostMapping
    public String createContact(Authentication authentication, @RequestBody ContactRequestDto contactRequestDto){
        String userEmail = authentication.getName();

        contactService.createContact(contactRequestDto, userEmail);
        return "Contact created successfully";
    }

    //Read
    @Operation(
            summary = "비상연락망 모두 조회",
            description = "로그인한 유저 정보에 맞는 비상연락망을 모두 조회합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포에 저장된 contact를 모두 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<ContactResponseDto>> getContactByUserEmail(Authentication authentication){
        String userEmail = authentication.getName();

        List<Contact> contacts = contactService.getContactByUserEmail(userEmail);
        List<ContactResponseDto> contactResponseDtos = new ArrayList<>();
        for (Contact contact : contacts) {
            ContactResponseDto dto = ContactResponseDto.from(contact);
            contactResponseDtos.add(dto);
        }
        return ResponseEntity.ok(contactResponseDtos);
    }

    //Update
    @Operation(
            summary = "비상연락망 수정",
            description = "유저인포에 따른 비상연락망을 수정합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 contact가 맞는 경우 비상연락망을 수정합니다.",
            parameters = {
                    @Parameter(name = "contactId", description = "[path_variable] contact의 Id", required = true)
            }
    )
    @PatchMapping("/{contactId}")
    public ResponseEntity<ContactResponseDto> updateContact(@PathVariable Long id, @RequestBody ContactRequestDto contactRequestDto){
        try{
            Contact updateContact = contactService.updateContact(id, contactRequestDto);
            return ResponseEntity.ok(ContactResponseDto.from(updateContact));
        } catch (RuntimeException e){
            return ResponseEntity.notFound().build();
        }
    }

    //Delete
    @Operation(
            summary = "비상연락망 삭제",
            description = "유저인포에 따른 비상연락망을 삭제합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며 " +
                    "해당 유저의 유저인포의 contact가 맞는 경우 비상연락망을 삭제합니다.",
            parameters = {
                    @Parameter(name = "contactId", description = "[path_variable] contact의 Id", required = true)
            }
    )
    @DeleteMapping("/{contactId}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id){
        contactService.deleteContact(id);
        return ResponseEntity.noContent().build();
    }

}
