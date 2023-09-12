package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Getter
@Setter
@Entity
@Table(name = "author")
@Proxy(lazy = false)
public class Author {
    @Id
    @Column(name = "author", nullable = false, length = Integer.MAX_VALUE)
    private String author;

    @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "author", nullable = false)
    private Account account;

    @Column(name = "author_id", length = Integer.MAX_VALUE)
    private String authorId;
    @Column(name = "phone", length = 11)
    private String phone;

    @Column(name = "author_registration_time", columnDefinition = "timestamp(0)")
    private String authorRegistrationTime;

}