package org.ziad.mutlitenantsaas.security.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import jakarta.annotation.Nonnull;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.ziad.mutlitenantsaas.exception.UnauthorizedException;
import org.ziad.mutlitenantsaas.properties.JwtProperties;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

/**
 * Service responsible for generating, validating and parsing RS256-signed JWT tokens.
 *
 * <p>Uses an RSA key pair loaded from the classpath at startup:
 * <ul>
 *   <li>Private key ({@code PKCS#8 PEM}) — used to <em>sign</em> tokens.</li>
 *   <li>Public key ({@code X.509 PEM})  — used to <em>verify</em> token signatures.</li>
 * </ul>
 * Key paths are configured via {@link JwtProperties} ({@code app.jwt.private-key-path}
 * and {@code app.jwt.public-key-path}).
 *
 * <p>Each JWT payload carries:
 * <ul>
 *   <li>{@code sub}       — the user's UUID (subject)</li>
 *   <li>{@code tenant_id} — the tenant UUID the user belongs to</li>
 *   <li>{@code role}      — the user's {@link org.ziad.mutlitenantsaas.entity.UserRole} name</li>
 *   <li>{@code iat}       — issued-at timestamp</li>
 *   <li>{@code exp}       — expiration timestamp</li>
 *   <li>{@code iss}       — issuer ({@code "stock-saas-app"})</li>
 * </ul>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenService {

    private final JwtProperties jwtProperties;
    private PrivateKey privateKey;
    private PublicKey publicKey;

    /**
     * Loads the RSA private and public keys from the classpath at application startup.
     *
     * @throws RuntimeException if either key file cannot be found or parsed
     */
    @PostConstruct
    public void init() {
        try {
            this.privateKey = loadPrivateKey(this.jwtProperties.getPrivateKeyPath());
            this.publicKey = loadPublicKey(this.jwtProperties.getPublicKeyPath());

            log.info("Private & Public key loaded successfully");
        } catch (final Exception e) {
            log.error("Error loading private key", e);
            throw new RuntimeException("Error loading private key", e);
        }
    }

    /**
     * Generates a signed RS256 JWT access token for the given user.
     *
     * @param tenantId the UUID of the user's tenant; embedded as the {@code tenant_id} claim
     * @param userId   the UUID of the authenticated user; used as the JWT subject
     * @param role     the user's role name (e.g. {@code "ROLE_COMPANY_ADMIN"})
     * @return compact, Base64URL-encoded signed JWT string
     */
    public String generateAccessToken(
            @Nonnull final String tenantId,
            @Nonnull final String userId,
            final String role) {
        final Date now = new Date();
        final Date expiration = new Date(System.currentTimeMillis() + this.jwtProperties.getAccessTokenExpiration());

        return Jwts.builder()
                .subject(userId)
                .claim("tenant_id", tenantId)
                .claim("role", role)
                .issuedAt(now)
                .expiration(expiration)
                .issuer("stock-saas-app")
                .signWith(this.privateKey, Jwts.SIG.RS256)
                .compact();

    }

    /**
     * Extracts the user ID (JWT subject) from a valid token.
     *
     * @param token the compact JWT string
     * @return the user UUID stored as the {@code sub} claim
     */
    public String getUserIdFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.getSubject();
    }

    /**
     * Extracts the tenant ID from a valid token.
     *
     * @param token the compact JWT string
     * @return the tenant UUID stored in the {@code tenant_id} claim
     */
    public String getTenantIdFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.get("tenant_id", String.class);
    }

    /**
     * Extracts the role from a valid token.
     *
     * @param token the compact JWT string
     * @return the role name stored in the {@code role} claim
     */
    public String getRoleFromToken(final String token) {
        final Claims claims = getClaimsFromToken(token);
        return claims.get("role", String.class);
    }

    /**
     * Validates a JWT token's signature and expiry.
     *
     * @param token the compact JWT string
     * @return {@code true} if the token is valid and not expired
     * @throws org.ziad.mutlitenantsaas.exception.UnauthorizedException for expired,
     *         malformed, unsigned or otherwise invalid tokens
     */
    public boolean validateToken(final String token) {
        try {
            Jwts.parser()
                    .verifyWith(this.publicKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (final ExpiredJwtException e) {
            throw new UnauthorizedException("Token has expired");
        } catch (final UnsupportedOperationException e) {
            throw new UnauthorizedException("Token is not signed");
        } catch (final MalformedJwtException e) {
            throw new UnauthorizedException("Token is malformed");
        } catch (final SecurityException e) {
            throw new UnauthorizedException("Invalid JWT Signature");
        } catch (final IllegalArgumentException e) {
            throw new UnauthorizedException("JWT claims string is empty");
        }
    }

    private Claims getClaimsFromToken(final String token) {
        return Jwts.parser()
                .verifyWith(this.publicKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    private PrivateKey loadPrivateKey(final String privateKeyPath) throws Exception {
        try (final InputStream is = JwtTokenService.class.getClassLoader()
                .getResourceAsStream(privateKeyPath)) {

            if (is == null) {
                throw new RuntimeException("Private key not found");
            }

            final String key = new String(is.readAllBytes());
            final String privateKeyPEM = key
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            final byte[] encoded = Base64.getDecoder()
                    .decode(privateKeyPEM);
            final PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encoded);
            return KeyFactory.getInstance("RSA")
                    .generatePrivate(keySpec);
        }
    }

    private PublicKey loadPublicKey(final String publicKeyPath) throws Exception {
        try (final InputStream is = JwtTokenService.class.getClassLoader()
                .getResourceAsStream(publicKeyPath)) {

            if (is == null) {
                throw new RuntimeException("Public key not found");
            }

            final String key = new String(is.readAllBytes());
            final String publicKeyPEM = key
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            final byte[] encoded = Base64.getDecoder()
                    .decode(publicKeyPEM);
            final X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encoded);
            return KeyFactory.getInstance("RSA")
                    .generatePublic(keySpec);
        }
    }
}