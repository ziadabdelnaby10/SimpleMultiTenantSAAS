package org.ziad.mutlitenantsaas.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.ziad.mutlitenantsaas.service.PasswordEncryptionService;

@Service
@RequiredArgsConstructor
@Slf4j
public class PasswordEncryptionServiceImpl implements PasswordEncryptionService {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String encryptPassword(final String password) {
        return passwordEncoder.encode(password);
    }

    @Override
    public String decryptPassword(String encryptedPassword) {
        throw new UnsupportedOperationException("Decrypting password is not supported for security reasons");
    }

    @Override
    public Boolean checkPassword(String password, String encryptedPassword) {
        return passwordEncoder.matches(password, encryptedPassword);
    }
}
