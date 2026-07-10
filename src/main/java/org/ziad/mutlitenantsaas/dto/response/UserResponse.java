package org.ziad.mutlitenantsaas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.ziad.mutlitenantsaas.entity.UserRole;

@Schema(description = "User details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    @Schema(description = "Unique user identifier", accessMode = Schema.AccessMode.READ_ONLY, example = "user-789")
    private String id;

    @Schema(description = "User email address", accessMode = Schema.AccessMode.READ_ONLY, example = "jane.smith@acme.com")
    private String email;

    @Schema(description = "Username", accessMode = Schema.AccessMode.READ_ONLY, example = "jane.smith")
    private String username;

    @Schema(description = "First name", accessMode = Schema.AccessMode.READ_ONLY, example = "Jane")
    private String firstName;

    @Schema(description = "Last name", accessMode = Schema.AccessMode.READ_ONLY, example = "Smith")
    private String lastName;

    @Schema(description = "Assigned role", accessMode = Schema.AccessMode.READ_ONLY, example = "ROLE_USER")
    private UserRole role;

}
