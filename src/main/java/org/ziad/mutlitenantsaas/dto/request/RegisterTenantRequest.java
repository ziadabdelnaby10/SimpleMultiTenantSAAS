package org.ziad.mutlitenantsaas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

@Schema(description = "New tenant registration payload")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterTenantRequest {

    @Schema(description = "Legal name of the company", example = "Acme Corporation")
    @NotBlank(message = "Company name should not be empty")
    @Size(max = 150, message = "Company name must not exceed 150 characters")
    private String companyName;

    @Schema(description = "Unique alphanumeric code identifying the tenant (3–30 chars, underscores and hyphens allowed)", example = "acme-corp")
    @NotBlank(message = "Company code should not be empty")
    @Pattern(regexp = "^[a-zA-Z0-9_-]{3,30}$",
            message = "Company code must be 3–30 alphanumeric characters (underscores and hyphens allowed, no spaces)")
    private String companyCode;

    @Schema(description = "Primary company contact email", example = "contact@acme.com")
    @NotBlank(message = "Email should not be empty")
    @Email(message = "Email must be a valid email address")
    private String email;

    @Schema(description = "Full name of the initial admin user", example = "John Doe")
    @NotBlank(message = "Admin full name should not be empty")
    @Size(max = 150, message = "Admin full name must not exceed 150 characters")
    private String adminFullName;

    @Schema(description = "Email address of the initial admin user", example = "admin@acme.com")
    @NotBlank(message = "Admin email should not be empty")
    @Email(message = "Admin email must be a valid email address")
    private String adminEmail;

    @Schema(description = "Username for the initial admin account (3–50 chars)", example = "john.admin")
    @NotBlank(message = "Admin username should not be empty")
    @Size(min = 3, max = 50, message = "Admin username must be between 3 and 50 characters")
    private String adminUsername;

    @Schema(description = "Password for the initial admin account (min 8 chars)", example = "Admin@1234", format = "password")
    @NotBlank(message = "Admin password should not be empty")
    @Size(min = 8, max = 128, message = "Admin password must be between 8 and 128 characters")
    private String adminPassword;
}