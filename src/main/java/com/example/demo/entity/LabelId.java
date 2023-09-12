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
public class LabelId implements Serializable {
    private static final long serialVersionUID = -5704441855870106241L;
    @Column(name = "category", nullable = false, length = Integer.MAX_VALUE)
    private String category;

    @Column(name = "post_id", nullable = false)
    private Integer postId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        LabelId entity = (LabelId) o;
        return Objects.equals(this.postId, entity.postId) &&
                Objects.equals(this.category, entity.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(postId, category);
    }

}