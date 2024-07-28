package com.skuteam3.fourj.account.controller;

import com.skuteam3.fourj.account.dto.RegisterUserDto;
import com.skuteam3.fourj.account.service.RegisterUserService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController()
@RequestMapping("/api/v1")
@Slf4j
public class UserController {

    private final RegisterUserService userService;

    @PostMapping("/users")
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
