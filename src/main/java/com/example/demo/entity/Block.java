package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "block")
public class Block {
    @EmbeddedId
    private BlockId id;

    @MapsId("block")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "block", nullable = false)
    private Account block;

    @MapsId("blocked")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "blocked", nullable = false)
    private Account blocked;

}