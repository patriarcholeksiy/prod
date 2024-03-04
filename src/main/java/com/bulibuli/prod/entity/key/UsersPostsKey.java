package com.bulibuli.prod.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class UsersPostsKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "post_id")
    Long postId;


}
