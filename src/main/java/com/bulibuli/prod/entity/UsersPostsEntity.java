package com.bulibuli.prod.entity;

import com.bulibuli.prod.entity.key.UsersPostsKey;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "users_posts")
public class UsersPostsEntity {

    @EmbeddedId
    UsersPostsKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @MapsId("postId")
    @JoinColumn(name = "post_id")
    PostEntity post;

}
