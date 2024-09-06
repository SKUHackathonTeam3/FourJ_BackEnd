package com.skuteam3.fourj.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class ExistsFcmClientKeyRequestDto {

    @Schema(description="클라이언트 FCM key 존재 여부", example="false")
    private boolean existsClientFcmKey;
}
