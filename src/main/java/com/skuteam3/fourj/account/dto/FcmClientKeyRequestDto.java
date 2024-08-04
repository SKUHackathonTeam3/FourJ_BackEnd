package com.skuteam3.fourj.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FcmClientKeyRequestDto {

    @Schema(description="클라이언트 FCM key", example="YOUR-CLIENT-FCM-KEY")
    private String clientFcmKey;
}
