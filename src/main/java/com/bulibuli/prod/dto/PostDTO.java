package com.bulibuli.prod.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
public class PostDTO {

    private Long id;

    private String content;

    private List<String> tags;

    private String creatorLogin;

    private LocalDateTime createdAt;

    private int likesCount;

    private int dislikesCount;

}
