package com.syook.service;


import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class AesCryptoService {

    private static final String ALGO = "AES/CTR/NoPadding";
    private static final int IV_SIZE = 16;
    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 65_536;
    private static final String AES = "AES";
    private static final String HMAC = "PBKDF2WithHmacSHA256";

    private final SecretKey secretKey;

    public AesCryptoService(@Value("${crypto.secret}") String secret, @Value("${crypto.salt}") String salt) {
        this.secretKey = deriveKey(secret, salt);
    }

    private SecretKey deriveKey(String password, String salt) {

        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance(HMAC);

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), ITERATIONS, KEY_SIZE);

            return new SecretKeySpec(factory.generateSecret(spec).getEncoded(), AES);

        } catch (Exception e) {
            throw new IllegalStateException("Key derivation failed", e);
        }
    }

    public String decrypt(String encrypted) {

        try {
            byte[] combined = Base64.getDecoder().decode(encrypted);

            byte[] iv = Arrays.copyOfRange(combined, 0, IV_SIZE);
            byte[] cipherText = Arrays.copyOfRange(combined, IV_SIZE, combined.length);

            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, new IvParameterSpec(iv));

            byte[] plain = cipher.doFinal(cipherText);

            return new String(plain, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Decrypt failed", e);
        }
    }

    public String sha256(String input) {

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    input.getBytes(StandardCharsets.UTF_8)
            );

            return HexFormat.of().formatHex(hash);

        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}
