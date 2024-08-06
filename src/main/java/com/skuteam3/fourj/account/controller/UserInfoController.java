package com.skuteam3.fourj.account.controller;

import com.skuteam3.fourj.account.dto.*;
import com.skuteam3.fourj.account.service.UserInfoService;
import com.skuteam3.fourj.global.message.dto.JsonMessageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@Tag(name="userInfos", description="유저 정보 API")
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-infos")
@RestController
public class UserInfoController {

    private final UserInfoService userInfoService;

    @Operation(
            summary = "유저 닉네임 조회",
            description = "유저의 닉네임을 조회 합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 닉네임을 반환합니다. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "FCM Client Key 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserNameDto.class)
                            )))})
    @GetMapping("/user-name")
    public ResponseEntity<?> getUserInfoName(Authentication authentication) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(userInfoService.getUserInfoName(userEmail));
    }

    @Operation(
            summary = "유저 닉네임 조회",
            description = "유저의 닉네임을 조회 합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 닉네임을 반환합니다. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "FCM Client Key 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = UserAbtiDto.class)
                            )))})
    @GetMapping("/user-abti")
    public ResponseEntity<?> getUserInfoAbti(Authentication authentication) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok().body(userInfoService.getUserInfoAbti(userEmail));
    }

    // 유저 닉네임 수정
    @Operation(
            summary = "유저 닉네임 수정",
            description = "로그인한 유저의 닉네임을 수정합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 닉네임을 Example 형식의 Json 데이터로 수정합니다. "
    )
    @PatchMapping("/user-name")
    public ResponseEntity<?> updateUserInfoName(Authentication authentication, @RequestBody UserNameDto userNameDto) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

            userInfoService.updateUserInfoName(userEmail, userNameDto);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to update user name: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new JsonMessageResponseDto("Failed to update user name"));
        }

        return ResponseEntity.ok(new JsonMessageResponseDto("Success to update user name"));
    }

    // 유저 음주량 수정
    @Operation(
            summary = "유저 주량 및 주간 음주량 수정",
            description = "로그인한 유저의 주량 및 주간 음주량을 수정합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 주량 및 주간 음주량을 Example 형식의 Json 데이터로 수정합니다. "
    )
    @PatchMapping("/user-drink-amount")
    public ResponseEntity<?> updateUserInfoDrinkAmount(Authentication authentication, @RequestBody UpdateDrinkAmountDto updateDrinkAmountDto) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        try {

            userInfoService.updateUserInfoDrinkAmount(userEmail, updateDrinkAmountDto);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to update user drink amount: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new JsonMessageResponseDto("Failed to update user drink amount"));
        }

        return ResponseEntity.ok(new JsonMessageResponseDto("Success to update user drink amount"));
    }

    @Operation(
            summary = "유저 FCM Client Key 설정 여부 조회",
            description = "유저의 FCM Client Key가 설정되었는지를 조회 합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 FCM Client Key 값의 존재 여부를 boolean값으로 반환합니다. "
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "FCM Client Key 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = ExistsFcmClientKeyRequestDto.class)
                            )))})
    @GetMapping("/fcm-key")
    public ResponseEntity<?> existsFcmClientKey(Authentication authentication) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(userInfoService.existsUserInfoFcmKey(userEmail));
    }

    @Operation(
            summary = "유저 FCM Client Key 수정",
            description = "유저의 FCM Client Key 값을 수정합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 FCM Client Key 값을 수정합니다. "
    )
    @PatchMapping("/fcm-key")
    public ResponseEntity<?> updateFcmClientKey(Authentication authentication, @RequestBody FcmClientKeyRequestDto fcmClientKeyRequestDto) {

        String userEmail;
        try {

            userEmail = authentication.getName();
        } catch (Exception e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        userInfoService.setUserInfoFcmKey(userEmail, fcmClientKeyRequestDto);
        return ResponseEntity.ok().build();
    }
}
