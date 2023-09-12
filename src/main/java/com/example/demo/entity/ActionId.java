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
public class ActionId implements Serializable {
    private static final long serialVersionUID = 7681568382144974695L;
    @Column(name = "post_id", nullable = false)
    private Integer postId;

    @Column(name = "name", nullable = false, length = Integer.MAX_VALUE)
    private String name;

    @Column(name = "type", nullable = false, length = Integer.MAX_VALUE)
    private String type;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ActionId entity = (ActionId) o;
        return Objects.equals(this.name, entity.name) &&
                Objects.equals(this.postId, entity.postId) &&
                Objects.equals(this.type, entity.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, postId, type);
    }

}