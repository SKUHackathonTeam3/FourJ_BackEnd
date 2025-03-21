package com.skuteam3.fourj.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UserNameDto {

    @Schema(description ="이름", example="김서경")
    private String name;
}
