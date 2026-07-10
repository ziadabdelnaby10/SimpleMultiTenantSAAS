package org.ziad.mutlitenantsaas.service;

import org.ziad.mutlitenantsaas.dto.request.LoginRequest;
import org.ziad.mutlitenantsaas.dto.response.LoginResponse;

/**
 * Service contract for authenticating platform users.
 */
public interface AuthenticationService {

    /**
     * Validates the supplied credentials and, if correct, issues a JWT access token.
     *
     * @param request contains the {@code username} and {@code password} to authenticate
     * @return a {@link LoginResponse} containing the signed JWT and token type
     * @throws org.springframework.security.core.AuthenticationException if the
     *         credentials are invalid
     */
    LoginResponse login(final LoginRequest request);
}
