package com.bulibuli.prod.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.Set;

@Data
public class CreatePostDTO {

    @NotBlank
    @Size(max = 1000)
    private String content;

    @NotBlank
    @Length(max = 20)
    private Set<String> tags;

}
