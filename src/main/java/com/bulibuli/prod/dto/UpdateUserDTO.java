package com.bulibuli.prod.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserDTO {

    @Pattern(regexp = "[a-zA-Z]{2}")
    private String countryAlpha2;

    private Boolean userIsPublic;

    @Size(max = 20)
    @Pattern(regexp = "\\+[\\d]+", message = "Неверный формат номер телефона")
    private String userPhone;

    @Size(max = 200)
    @Pattern(regexp = ".*\\.(jpg|jpeg|png|gif)")
    private String userImage;

}
