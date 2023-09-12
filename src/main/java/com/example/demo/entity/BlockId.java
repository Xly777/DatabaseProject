package com.example.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@Embeddable
public class BlockId implements Serializable {
    private static final long serialVersionUID = -5466910802362226622L;
    @Column(name = "block", nullable = false, length = Integer.MAX_VALUE)
    private String block;

    @Column(name = "blocked", nullable = false, length = Integer.MAX_VALUE)
    private String blocked;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        BlockId entity = (BlockId) o;
        return Objects.equals(this.blocked, entity.blocked) &&
                Objects.equals(this.block, entity.block);
    }

    @Override
    public int hashCode() {
        return Objects.hash(blocked, block);
    }

}