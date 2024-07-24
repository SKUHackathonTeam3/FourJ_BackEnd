package com.skuteam3.fourj.contact.dto;

import com.skuteam3.fourj.contact.domain.Contact;
import lombok.Data;

@Data
public class ContactDto {
    private String name;
    private String number;
    private Boolean isMain;

    public static ContactDto from(Contact contact){
        ContactDto contactDto = new ContactDto();

        contactDto.setName(contact.getName());
        contactDto.setNumber(contact.getNumber());
        contactDto.setIsMain(contact.getIsMain());
        return contactDto;
    }
}
