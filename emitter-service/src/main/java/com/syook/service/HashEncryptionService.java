package com.syook.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.HexFormat;

@Service
public class HashEncryptionService {

    private final SecretKey secretKey;
    private final SecureRandom secureRandom = new SecureRandom();

    private final String secrete;
    private final String salt;

    private static final String ALGO = "AES/CTR/NoPadding";
    private static final int IV_SIZE = 16;
    private static final int KEY_SIZE = 256;
    private static final int ITERATIONS = 65_536;


    public HashEncryptionService(@Value("${emitter.crypto.secret}") String secret, @Value("${emitter.crypto.salt}") String salt) {
        this.secrete = secret;
        this.salt = salt;
        this.secretKey = createSecretKey();
    }

    private SecretKey createSecretKey() {
        try {
            SecretKeyFactory factory =
                    SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");

            KeySpec spec = new PBEKeySpec(secrete.toCharArray(), salt.getBytes(), ITERATIONS, KEY_SIZE);

            byte[] keyBytes = factory.generateSecret(spec).getEncoded();
            return new SecretKeySpec(keyBytes, "AES");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public String encrypt(String plainText) {

        try {
            byte[] iv = secureRandom.generateSeed(IV_SIZE);

            Cipher cipher = Cipher.getInstance(ALGO);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(iv));

            byte[] encrypted = cipher.doFinal(plainText.getBytes());

            byte[] result = new byte[iv.length + encrypted.length];
            System.arraycopy(iv, 0, result, 0, iv.length);
            System.arraycopy(encrypted, 0, result, iv.length, encrypted.length);

            return Base64.getEncoder().encodeToString(result);

        } catch (Exception e) {
            throw new RuntimeException("Encryption failed", e);
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
