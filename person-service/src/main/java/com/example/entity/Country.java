package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import java.time.Instant;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.annotations.ColumnDefault;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "countries", schema = "person")
public class Country {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ColumnDefault("true")
    @Column(name = "active", nullable = false)
    private Boolean active;

    @ColumnDefault("(now() AT TIME ZONE 'utc'::text)")
    @Column(name = "created", nullable = false)
    private Instant created;

    @ColumnDefault("(now() AT TIME ZONE 'utc'::text)")
    @Column(name = "updated", nullable = false)
    private Instant updated;

    @Size(max = 32)

    @Column(name = "name", nullable = false, length = 32)
    private String name;

    @Size(max = 3)

    @Column(name = "code", nullable = false, length = 3)
    private String code;

}