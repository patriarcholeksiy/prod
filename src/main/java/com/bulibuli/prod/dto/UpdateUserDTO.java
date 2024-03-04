package com.bulibuli.prod.dto;

import lombok.Data;

@Data
public class UpdateUserDTO {

    private String alpha2;

    private Boolean isPublic;

    private String phoneNumber;

    private String image;

}
