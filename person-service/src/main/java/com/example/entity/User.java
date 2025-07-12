package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "users", schema = "person")
public class User extends HibernateEntity {

    @Size(max = 1024)
    @Column(name = "email", nullable = false, length = 1024)
    private String email;

    @Size(max = 32)
    @Column(name = "first_name", nullable = false, length = 32)
    private String firstName;

    @Size(max = 32)
    @Column(name = "last_name", nullable = false, length = 32)
    private String lastName;

    @ManyToOne(optional = false, cascade = {PERSIST, MERGE, REMOVE})
    @JoinColumn(name = "address_id", nullable = false)
    private Address address;
}