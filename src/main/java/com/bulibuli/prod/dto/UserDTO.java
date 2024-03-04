package com.bulibuli.prod.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String userLogin;

    private String userEmail;

    private String countryAlpha2;

    private boolean userIsPublic;

    private String userPhone;

    private String userImage;

}
