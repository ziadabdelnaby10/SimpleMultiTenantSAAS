package org.ziad.mutlitenantsaas.service;

/**
 * Service contract for password hashing and verification.
 *
 * <p>The implementation uses BCrypt one-way hashing; decryption is intentionally
 * unsupported for security reasons.
 */
public interface PasswordEncryptionService {

    /**
     * Hashes a plain-text password using BCrypt.
     *
     * @param password the raw password to hash
     * @return the BCrypt-encoded password hash
     */
    String encryptPassword(String password);

    /**
     * Not supported — BCrypt is a one-way function and cannot be reversed.
     *
     * @param encryptedPassword unused
     * @return never returns
     * @throws UnsupportedOperationException always
     */
    String decryptPassword(String encryptedPassword);

    /**
     * Checks whether a plain-text password matches a BCrypt-encoded hash.
     *
     * @param password          the raw password to verify
     * @param encryptedPassword the stored BCrypt hash
     * @return {@code true} if the password matches the hash, {@code false} otherwise
     */
    Boolean checkPassword(String password, String encryptedPassword);
}
