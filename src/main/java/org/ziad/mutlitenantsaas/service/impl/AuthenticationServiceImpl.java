package org.ziad.mutlitenantsaas.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.ziad.mutlitenantsaas.dto.request.LoginRequest;
import org.ziad.mutlitenantsaas.dto.response.LoginResponse;
import org.ziad.mutlitenantsaas.entity.User;
import org.ziad.mutlitenantsaas.security.config.JwtTokenService;
import org.ziad.mutlitenantsaas.service.AuthenticationService;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenService;

    @Override
    public LoginResponse login(final LoginRequest request) {
        final Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        final User user = (User) authentication.getPrincipal();

        final String token = this.jwtTokenService.generateAccessToken(user.getTenantId(),
                user.getId(),
                user.getRole()
                        .name());
        final String tokenType = "Bearer";

        return LoginResponse.builder()
                .accessToken(token)
                .tokenType(tokenType)
                .build();
    }
}
