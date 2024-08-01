package com.skuteam3.fourj.abti.controller;

import com.skuteam3.fourj.abti.dto.AbtiRequestDto;
import com.skuteam3.fourj.abti.dto.AbtiResponseDto;
import com.skuteam3.fourj.abti.service.AbtiService;
import com.skuteam3.fourj.global.message.dto.JsonMessageResponseDto;
import io.swagger.v3.core.util.Json;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

import java.net.URI;
import java.util.List;

@Tag(name="abtis", description="Abti API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AbtiController {

    private final AbtiService abtiService;

    // Abti 생성
    @Operation(
            summary = "Abti 생성",
            description = "Example 형식의 Json 데이터를 받아와, ABTI를 생성합니다."
    )
    @PostMapping("/abtis")
    public ResponseEntity<?> createAbti(@RequestBody AbtiRequestDto dto) {

        try {

            Long id = abtiService.createAbti(dto);

            return ResponseEntity.created(URI.create("/api/v1/abtis/" + id)).build();
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to create Abti: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new JsonMessageResponseDto("Failed to create Abti"));
        }
    }

    // 모든 Abti 조회
    @Operation(
            summary = "모든 ABTI 조회",
            description = "데이터베이스에 저장된 모든 abti를 조회합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Abtis returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AbtiResponseDto.class)
                    )

            ),
    })
    @GetMapping("/abtis")
    public ResponseEntity<?> getAllAbtis() {
        try {

            List<AbtiResponseDto> abtiResponseDtoList = abtiService.getAllAbti();

            return ResponseEntity.ok(abtiResponseDtoList);
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new JsonMessageResponseDto("Failed to get Abtis"));
        }
    }

    // Abti Id값으로 Abti 조회
    @Operation(
            summary = "ABTI ID에 해당하는 ABTI 조회",
            description = "abtiId에 해당하는 Abti 데이터를 조회합니다.",
            parameters = {
                    @Parameter(name = "abtiId", description = "[path_variable] Abti의 ID", required = true)
            }
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200", description = "Abtis returned successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AbtiResponseDto.class)
                    )

            ),
    })
    @GetMapping("/abtis/{abtiId}")
    public ResponseEntity<?> getAbtiById(@PathVariable Long abtiId) {

        try {

            AbtiResponseDto abtiResponseDto = abtiService.getAbtiById(abtiId);

            return ResponseEntity.ok(abtiResponseDto);
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to get Abti: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new JsonMessageResponseDto("Failed to get Abti"));
        }
    }

    // Abti 수정
    @Operation(
            summary = "ABTI 수정",
            description = "abtiId에 해당하는 Abti 데이터를 Example 형식의 Json 데이터를 받아와 수정합니다.",
            parameters = {
                    @Parameter(name = "abtiId", description = "[path_variable] Abti의 ID", required = true)
            }
    )
    @PatchMapping("/abtis/{abtiId}")
    public ResponseEntity<?> updateAbti(@PathVariable Long abtiId, @RequestBody AbtiRequestDto dto) {
        try {
            abtiService.updateAbtiById(abtiId, dto);

            return ResponseEntity.ok().build();
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to update Abti: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new JsonMessageResponseDto("Failed to update Abti"));
        }
    }

    // Abti 삭제
    @Operation(
            summary = "ABTI 삭제",
            description = "abtiId에 해당하는 ABTI데이터를 삭제합니다.",
            parameters = {
                    @Parameter(name = "abtiId", description = "[path_variable] Abti의 ID", required = true)
            }
    )
    @DeleteMapping("/abtis/{abtiId}")
    public ResponseEntity<?> deleteAbti(@PathVariable Long abtiId) {

        try {

            abtiService.deleteAbti(abtiId);

            return ResponseEntity.noContent().build();
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to delete Abti: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new JsonMessageResponseDto("Failed to delete Abti"));
        }
    }

    // 유저 인포에 Abti 추가 || 수정
    @Operation(
            summary = "현재 로그인한 유저의 유저인포에 ABTI 정보 저장 또는 수정",
            description = "로그인한 ABTI 정보를 저장하거나, 수정할 수 있습니다." +
                    "헤더의 Authorization필드에 적재된 JWT토큰을 이용하여 회원 정보를 받아오며" +
                    "해당 유저의 유저인포에 ABTI 필드에 Abti의 ID값을 저장합니다.",
            parameters = {
                    @Parameter(name = "abtiId", description = "[path_variable] Abti의 ID", required = true)
            }
    )
    @PatchMapping("/abtis/userinfos/{abtiId}")
    public ResponseEntity<?> updateUserInfoAbti(Authentication authentication, @PathVariable Long abtiId) {

        String userEmail;
        try {

            try {

                userEmail = authentication.getName();
            } catch (Exception e) {

                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            }

            abtiService.setUserInfoAbti(userEmail, abtiId);

            return ResponseEntity.ok("Success to update Abti.");
        } catch (ResponseStatusException rse) {

            return ResponseEntity.status(rse.getStatusCode()).body(new JsonMessageResponseDto("Failed to update Abti: " + rse.getMessage()));
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body(new JsonMessageResponseDto("Failed to update Abti"));
        }
    }



}
