package com.bulibuli.prod.repository;

import com.bulibuli.prod.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagsRepository extends JpaRepository<TagEntity, String> {
}
