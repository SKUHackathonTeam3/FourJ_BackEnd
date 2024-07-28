package com.skuteam3.fourj.account.controller;

import com.skuteam3.fourj.account.dto.UpdateDrinkAmountDto;
import com.skuteam3.fourj.account.dto.UpdateNameDto;
import com.skuteam3.fourj.account.service.UserInfoService;
import com.skuteam3.fourj.global.message.dto.JsonMessageResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

    // 유저 닉네임 수정
    @Operation(
            summary = "유저 닉네임 수정",
            description = "로그인한 유저의 닉네임을 수정합니다. " +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며, " +
                    "해당 유저의 닉네임을 Example 형식의 Json 데이터로 수정합니다. "
    )
    @PatchMapping("/user-name")
    public ResponseEntity<?> updateUserInfoName(Authentication authentication, @RequestBody UpdateNameDto updateNameDto) {

        String userEmail = authentication.getName();

        try {

            userInfoService.updateUserInfoName(userEmail, updateNameDto);
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
        String userEmail = authentication.getName();

        try {

            userInfoService.updateUserInfoDrinkAmount(userEmail, updateDrinkAmountDto);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to update user drink amount: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new JsonMessageResponseDto("Failed to update user drink amount"));
        }

        return ResponseEntity.ok(new JsonMessageResponseDto("Success to update user drink amount"));
    }
}
