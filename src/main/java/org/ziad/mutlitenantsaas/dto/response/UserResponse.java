package org.ziad.mutlitenantsaas.dto.response;

import lombok.*;
import org.ziad.mutlitenantsaas.entity.UserRole;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponse {

    private String id;

    private String email;

    private String username;

    private String firstName;

    private String lastName;

    private UserRole role;

}
