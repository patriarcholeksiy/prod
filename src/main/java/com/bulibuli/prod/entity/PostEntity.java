package com.bulibuli.prod.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity
@Table(name = "posts")
public class PostEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToMany
    private Set<TagEntity> tags;

    @ManyToOne
    private UserEntity creator;

    @ManyToMany
    @JoinTable(
            name = "posts_emotions",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> usersEmotions;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @Min(0)
    private int likes;

    @Min(0)
    private int dislikes;

}
