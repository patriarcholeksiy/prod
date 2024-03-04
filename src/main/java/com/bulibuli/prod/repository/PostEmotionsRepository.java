package com.bulibuli.prod.repository;

import com.bulibuli.prod.entity.PostEmotionsEntity;
import com.bulibuli.prod.entity.key.UsersPostsKey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostEmotionsRepository extends JpaRepository<PostEmotionsEntity, UsersPostsKey> {

    Optional<PostEmotionsEntity> getByUser_IdAndPost_Id(Long userId, Long postId);

    int countAllByPost_IdAndLikes(Long postId, boolean likes);

    int countAllByPost_IdAndDislikes(Long postId, boolean likes);
}
