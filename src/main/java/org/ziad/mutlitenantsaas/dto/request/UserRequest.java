package org.ziad.mutlitenantsaas.dto.request;

import lombok.*;
import org.ziad.mutlitenantsaas.entity.UserRole;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

    private String username;

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private UserRole role;

}
