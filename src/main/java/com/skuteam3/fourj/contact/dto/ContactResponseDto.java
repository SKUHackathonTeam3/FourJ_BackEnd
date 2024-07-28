package com.skuteam3.fourj.contact.dto;

import com.skuteam3.fourj.contact.domain.Contact;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ContactResponseDto {
    @Schema(description = "비상연락망 ID", example = "1")
    private Long id;
    @Schema(description = "비상연락망 이름", example = "김서경")
    private String name;
    @Schema(description = "전화번호", example = "010-0000-0000")
    private String number;
    @Schema(description = "알림 비상연락망 지정", example = "false")
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
