package org.ziad.mutlitenantsaas.service;

import org.ziad.mutlitenantsaas.dto.request.LoginRequest;
import org.ziad.mutlitenantsaas.dto.response.LoginResponse;

public interface AuthenticationService {
    LoginResponse login(final LoginRequest request);
}
