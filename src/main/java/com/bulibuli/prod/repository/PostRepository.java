package com.bulibuli.prod.repository;

import com.bulibuli.prod.entity.PostEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<PostEntity, Long> {

    List<PostEntity> getAllByCreator_IdOrderByCreatedAtDesc(Long creatorId, Pageable pageable);

}
