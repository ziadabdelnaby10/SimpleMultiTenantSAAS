package org.ziad.mutlitenantsaas.dto.response;

import lombok.*;
import org.ziad.mutlitenantsaas.entity.TenantStatus;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantResponse {
    private String tenantId;
    private String companyName;
    private String companyCode;
    private String email;
    private String adminFullName;
    private String adminEmail;
    private String adminUsername;
    private String adminPassword;
    private Instant createdAt;
    private TenantStatus status;
}