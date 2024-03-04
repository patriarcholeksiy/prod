package com.bulibuli.prod.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Set;

@Data
public class CreatePostDTO {

    @NotBlank
    private String content;

    @NotBlank
    private Set<String> tags;

}
