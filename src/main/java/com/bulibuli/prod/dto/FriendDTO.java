package com.bulibuli.prod.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class FriendDTO {

    private String login;

    private LocalDateTime addedAt;

}
