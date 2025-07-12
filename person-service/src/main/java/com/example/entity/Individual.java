package com.example.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.hibernate.envers.Audited;
import org.hibernate.envers.RelationTargetAuditMode;

import static jakarta.persistence.CascadeType.MERGE;
import static jakarta.persistence.CascadeType.PERSIST;
import static jakarta.persistence.CascadeType.REMOVE;

@Audited(targetAuditMode = RelationTargetAuditMode.NOT_AUDITED)
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "individuals", schema = "person")
public class Individual extends HibernateEntity {

    @Size(max = 32)
    @Column(name = "passport_number", nullable = false, length = 32)
    private String passportNumber;

    @Size(max = 32)
    @Column(name = "phone_number", nullable = false, length = 32)
    private String phoneNumber;

    @OneToOne(optional = false, cascade = {PERSIST, MERGE, REMOVE})
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}