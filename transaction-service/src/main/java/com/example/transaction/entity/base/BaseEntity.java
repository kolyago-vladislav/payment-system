package com.example.transaction.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

import com.example.transaction.util.CustomInstantDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id")
    private UUID id;

    @JsonDeserialize(using = CustomInstantDeserializer.class)
    @Column(name = "created")
    private Instant created;

    @JsonDeserialize(using = CustomInstantDeserializer.class)
    @Column(name = "updated")
    private Instant updated;

}
