package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Proxy;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
@Proxy(lazy = false)
public class FollowerId implements Serializable {
    private static final long serialVersionUID = -3969319349640560500L;
    @Column(name = "author", nullable = false, length = Integer.MAX_VALUE)
    private String author;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        FollowerId entity = (FollowerId) o;
        return Objects.equals(this.author, entity.author) &&
                Objects.equals(this.name, entity.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(author, name);
    }

}