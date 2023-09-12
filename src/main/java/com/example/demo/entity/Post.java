package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "posts")
@Proxy(lazy = false)
public class Post {
    @Id
    @Column(name = "post_id", nullable = false)
    private Integer id;

    @Column(name = "title", nullable = false, length = Integer.MAX_VALUE)
    private String title;

    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

    @Column(name = "posting_time")
    private Instant postingTime;

    @Column(name = "posting_city", length = Integer.MAX_VALUE)
    private String postingCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private Author author;

    @Column(name = "anonymous")
    private Boolean anonymous;

    @Column(name = "viewcount")
    private Integer viewcount;

}