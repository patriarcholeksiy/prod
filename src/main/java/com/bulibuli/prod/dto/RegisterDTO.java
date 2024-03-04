package com.bulibuli.prod.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank
    @Size(max = 30)
    @Pattern(regexp = "[a-zA-Z0-9-]+")
    private String login;

    @Email
    @NotBlank
    @Size(min = 1, max = 50)
    private String email;

    @NotBlank
    @Size(min = 6)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+")
    private String password;

    @NotBlank
    @Size(max = 2)
    @Pattern(regexp = "[a-zA-Z]{2}")
    private String alpha2;

    private Boolean isPublic;

    @NotBlank
    @Size(max = 20)
    @Pattern(regexp = "\\+[\\d]+", message = "Неверный формат номер телефона")
    private String phoneNumber;

    @Size(max = 200)
    @Pattern(regexp = ".*\\.(jpg|jpeg|png|gif)")
    private String image;

}
