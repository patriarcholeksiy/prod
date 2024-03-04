package com.bulibuli.prod.repository;

import com.bulibuli.prod.entity.UsersFriendsEntity;
import com.bulibuli.prod.entity.key.UsersFriendsKey;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

public interface UsersFriendsRepository extends JpaRepository<UsersFriendsEntity, UsersFriendsKey> {
    List<UsersFriendsEntity> getAllByUser_IdOrderByDateTimeDesc(Long userId, Pageable pageable);
}
