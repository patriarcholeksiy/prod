package com.bulibuli.prod.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterDTO {

    @NotBlank
    @Size(min = 3, max = 30)
    private String login;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Size(min = 2, max = 2)
    private String alpha2;

    private boolean isPublic;

    @NotBlank
    @Size(min = 4, max = 25)
    private String phoneNumber;

    @Size(max = 40)
    private String image;

}
