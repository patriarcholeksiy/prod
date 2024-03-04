package com.bulibuli.prod.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PostDTO {

    private Long postId;

    private String postContent;

    private List<String> postTags;

    private String userLogin;

    private LocalDateTime createdAt;

    private int likesCount;

    private int dislikesCount;

}
