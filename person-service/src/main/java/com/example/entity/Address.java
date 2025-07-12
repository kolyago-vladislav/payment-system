package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "addresses", schema = "person")
public class Address extends HibernateEntity {


    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Size(max = 128)
    @Column(name = "address", nullable = false, length = 128)
    private String address;

    @Size(max = 32)
    @Column(name = "zip_code", nullable = false, length = 32)
    private String zipCode;

    @Size(max = 32)
    @Column(name = "city", nullable = false, length = 32)
    private String city;

}