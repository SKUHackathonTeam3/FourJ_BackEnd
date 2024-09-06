package com.skuteam3.fourj.account.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDto {

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "Invalid email format.")
    @Schema(description="이메일", example="email@email.com")
    private String email;

    @Schema(description="비밀번호", example="password")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Schema(description="이름", example="김서경")
    private String name;
}
