package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Getter
@Setter
@Entity
@Table(name = "reply")
@Proxy(lazy = false)
public class Reply {
    @Id
    @Column(name = "reply_id", nullable = false)
    private Integer id;

    @Column(name = "father_id")
    private Integer fatherId;

    @Column(name = "content", length = Integer.MAX_VALUE)
    private String content;

    @Column(name = "stars")
    private Integer stars;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author")
    private Account author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

}