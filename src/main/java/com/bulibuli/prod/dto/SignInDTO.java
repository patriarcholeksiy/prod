package com.bulibuli.prod.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class SignInDTO {

    @Size(min = 4, max = 30, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String login;

    @Size(min = 8, message = "Пароль должен быть от 8 символов")
    @NotBlank(message = "Пароль не может быть пустым")
    private String password;

}
