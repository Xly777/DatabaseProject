package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Getter
@Setter
@Entity
@Table(name = "follower")
@Proxy(lazy = false)
public class Follower {
    @EmbeddedId
    private FollowerId id;

    @MapsId("author")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private Author author;

    @MapsId("name")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "name", nullable = false)
    private Account name;

}