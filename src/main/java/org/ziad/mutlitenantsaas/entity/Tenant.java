package org.ziad.mutlitenantsaas.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.ziad.mutlitenantsaas.common.AbstractEntity;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tenants")
public class Tenant extends AbstractEntity {

    @Column(nullable = false)
    private String companyName;

    @Column(nullable = false, unique = true)
    private String companyCode;

    @Column(nullable = false, unique = true)
    private String email;

    @Enumerated(STRING)
    @Column(nullable = false)
    private TenantStatus status = TenantStatus.PENDING;

    // initial admin credentials
    @Column(nullable = false)
    private String adminFullName;

    @Column(nullable = false, unique = true)
    private String adminEmail;

    @Column(nullable = false, unique = true)
    private String adminUsername;

    @Column(nullable = false)
    private String adminPassword;

}