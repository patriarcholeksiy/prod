package com.bulibuli.prod.repository;

import com.bulibuli.prod.entity.UsersPostsEntity;
import com.bulibuli.prod.entity.key.UsersPostsKey;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersPostsRepository extends JpaRepository<UsersPostsEntity, UsersPostsKey> {
    UsersPostsEntity getByPost_Id(Long postId);
}
