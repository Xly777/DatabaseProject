package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Proxy;

@Getter
@Setter
@Entity
@Table(name = "action")
@Proxy(lazy = false)
public class Action {
    @EmbeddedId
    private ActionId id;

    @MapsId("name")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "name", nullable = false)
    private Account name;

}