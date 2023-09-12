package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Getter
@Setter
@Entity
@Table(name = "account")
@Proxy(lazy = false)
public class Account {
    @Id
    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "password", length = Integer.MAX_VALUE)
    private String password;

    @Column(name = "account_id", length = Integer.MAX_VALUE)
    private String accountId;

    @Column(name = "registration_time", columnDefinition = "timestamp(0)")
    private String registrationTime;

    @Column(name = "privilege")
    private Integer privilege;

}