package org.ziad.mutlitenantsaas.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Schema(description = "Login credentials")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    @Schema(description = "Username of the account", example = "john.doe")
    @NotBlank(message = "Username should not be empty")
    private String username;

    @Schema(description = "Account password", example = "secureP@ss123", format = "password")
    @NotBlank(message = "Password should not be empty")
    private String password;
}