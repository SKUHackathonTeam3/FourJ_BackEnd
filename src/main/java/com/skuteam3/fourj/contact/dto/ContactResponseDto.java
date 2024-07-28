package com.skuteam3.fourj.contact.dto;

import com.skuteam3.fourj.contact.domain.Contact;
import lombok.Data;

@Data
public class ContactResponseDto {
    private Long id;
    private String name;
    private String number;
    private Boolean isMain;

    public static ContactResponseDto from(Contact contact){
        ContactResponseDto contactResponseDto = new ContactResponseDto();

        contactResponseDto.setId((contact.getId()));
        contactResponseDto.setName(contact.getName());
        contactResponseDto.setNumber(contact.getNumber());
        contactResponseDto.setIsMain(contact.getIsMain());
        return contactResponseDto;
    }
}
