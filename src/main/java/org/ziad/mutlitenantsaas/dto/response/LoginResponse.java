package org.ziad.mutlitenantsaas.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "Successful authentication response")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {

    @Schema(description = "JWT access token to include in the Authorization header of subsequent requests",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "eyJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZSJ9.signature")
    private String accessToken;

    @Schema(description = "Token type — always 'Bearer'",
            accessMode = Schema.AccessMode.READ_ONLY,
            example = "Bearer")
    private String tokenType;
}
