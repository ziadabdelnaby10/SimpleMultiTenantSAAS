package org.ziad.mutlitenantsaas.properties;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Strongly-typed configuration properties for JWT token generation and validation.
 *
 * <p>Bound from the {@code app.jwt} prefix in {@code application.yml}:
 * <pre>
 * app:
 *   jwt:
 *     private-key-path: certs/private_key.pem
 *     public-key-path:  certs/public_key.pem
 *     access-token-expiration: 86400000   # milliseconds (24 h)
 * </pre>
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    /** Classpath-relative path to the RSA PKCS#8 private key PEM file used to sign tokens. */
    private String privateKeyPath;

    /** Classpath-relative path to the RSA X.509 public key PEM file used to verify tokens. */
    private String publicKeyPath;

    /** Access token lifetime in milliseconds. Defaults to 86 400 000 ms (24 hours). */
    private long accessTokenExpiration;
}