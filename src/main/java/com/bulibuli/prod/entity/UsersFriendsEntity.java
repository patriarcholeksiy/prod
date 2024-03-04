package com.bulibuli.prod.entity;

import com.bulibuli.prod.entity.key.UsersFriendsKey;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users_friends")
public class UsersFriendsEntity {

    @EmbeddedId
    UsersFriendsKey id;

    @ManyToOne
    @MapsId("userId")
    @JoinColumn(name = "user_id")
    UserEntity user;

    @ManyToOne
    @MapsId("friendId")
    @JoinColumn(name = "friend_id")
    UserEntity friend;

    @CreationTimestamp
    private LocalDateTime dateTime;

}
