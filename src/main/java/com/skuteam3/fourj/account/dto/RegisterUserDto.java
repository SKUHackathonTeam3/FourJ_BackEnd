package com.skuteam3.fourj.account.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserDto {

    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "Invalid email format.")
    private String email;

    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    private String name;
}
