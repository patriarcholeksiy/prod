package com.bulibuli.prod.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tags")
public class TagEntity {

    @Id
    @Column(length = 20)
    private String tag;

}
