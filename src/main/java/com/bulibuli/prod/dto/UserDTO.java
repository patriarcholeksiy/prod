package com.bulibuli.prod.dto;

import lombok.Data;

@Data
public class UserDTO {

    private Long id;

    private String login;

    private String email;

    private String alpha2;

    private boolean isPublic;

    private String phoneNumber;

    private String image;

}
