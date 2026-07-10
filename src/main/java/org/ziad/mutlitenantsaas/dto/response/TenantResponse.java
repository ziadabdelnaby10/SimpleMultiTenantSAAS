package org.ziad.mutlitenantsaas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.ziad.mutlitenantsaas.entity.TenantStatus;

import java.time.Instant;

@Schema(description = "Tenant details")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TenantResponse {

    @Schema(description = "Unique tenant identifier", accessMode = Schema.AccessMode.READ_ONLY, example = "tenant-001")
    private String tenantId;

    @Schema(description = "Legal company name", accessMode = Schema.AccessMode.READ_ONLY, example = "Acme Corporation")
    private String companyName;

    @Schema(description = "Unique company code / slug", accessMode = Schema.AccessMode.READ_ONLY, example = "acme-corp")
    private String companyCode;

    @Schema(description = "Primary company contact email", accessMode = Schema.AccessMode.READ_ONLY, example = "contact@acme.com")
    private String email;

    @Schema(description = "Full name of the admin user", accessMode = Schema.AccessMode.READ_ONLY, example = "John Doe")
    private String adminFullName;

    @Schema(description = "Email address of the admin user", accessMode = Schema.AccessMode.READ_ONLY, example = "admin@acme.com")
    private String adminEmail;

    @Schema(description = "Username of the admin user", accessMode = Schema.AccessMode.READ_ONLY, example = "john.admin")
    private String adminUsername;

    @Schema(description = "Tenant registration timestamp (UTC)", accessMode = Schema.AccessMode.READ_ONLY, example = "2026-07-10T10:00:00Z")
    private Instant createdAt;

    @Schema(description = "Current status of the tenant", accessMode = Schema.AccessMode.READ_ONLY, example = "ACTIVE")
    private TenantStatus status;
}