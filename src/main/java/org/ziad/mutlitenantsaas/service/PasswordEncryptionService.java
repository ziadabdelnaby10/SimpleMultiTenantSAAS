package org.ziad.mutlitenantsaas.service;

public interface PasswordEncryptionService {

    String encryptPassword(String password);

    String decryptPassword(String encryptedPassword);

    Boolean checkPassword(String password, String encryptedPassword);
}
