package com.bulibuli.prod.entity.key;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;

@Data
@Embeddable
public class UsersFriendsKey implements Serializable {

    @Column(name = "user_id")
    Long userId;

    @Column(name = "friend_id")
    Long friendId;

}
