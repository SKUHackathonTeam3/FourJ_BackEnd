package com.skuteam3.fourj.account.controller;

import com.skuteam3.fourj.abti.dto.AbtiRequestDto;
import com.skuteam3.fourj.account.dto.RegisterUserDto;
import com.skuteam3.fourj.account.service.RegisterUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Tag(name="users", description="유저 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/users")
@Slf4j
public class UserController {

    private final RegisterUserService userService;

    @Operation(
            summary = "유저 회원 생성",
            description = "Example 형식의 Json 데이터로 회원을 생성합니다."
    )
    @PostMapping
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterUserDto dto, BindingResult bindingResult) {

        log.info(dto.toString());

        if (bindingResult.hasErrors()) {
            // 모든 에러 메시지를 추출
            List<String> errorMessages = bindingResult.getAllErrors()
                    .stream()
                    .map(DefaultMessageSourceResolvable::getDefaultMessage)
                    .collect(Collectors.toList());

            Map<String, List<String>> errorResponse = new HashMap<>();
            errorResponse.put("errors", errorMessages);

            return ResponseEntity.badRequest().body(errorResponse);
        }

        try {

            userService.save(dto);

            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {

            return ResponseEntity.badRequest().body("Email already exists");
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("Failed to register user");
        }
    }
}
