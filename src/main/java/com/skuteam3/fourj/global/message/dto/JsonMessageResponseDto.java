package com.skuteam3.fourj.global.message.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class JsonMessageResponseDto {

    private String message;

    public JsonMessageResponseDto(String message) {
        this.message = message;
    }
}
