package com.aaaaaa.petkovics.newcrypt;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;


public class Cryptor {
    private static Cryptor instance = null;
    private Cryptor() {}

    public static Cryptor getInstance()
    {
        if (instance == null)
            instance = new Cryptor();

        return instance;
    }

    private static final String KEYSTORE_PROVIDER = "AndroidKeyStore";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ALIAS = "MySecretKeyAlias";

    private static final String MAP_IV_KEY = "iv";
    private static final String MAP_ENCRYPTED_KEY = "encrypted";

    public void keygen() throws Exception {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, KEYSTORE_PROVIDER);
        KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder(ALIAS,  KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setRandomizedEncryptionRequired(true)
                .build();

        keyGenerator.init(keyGenParameterSpec);
        keyGenerator.generateKey();
    }

    private SecretKey getSecretKey() throws Exception{
        KeyStore keyStore = KeyStore.getInstance(KEYSTORE_PROVIDER);
        keyStore.load(null);

        KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(ALIAS, null);
        return secretKeyEntry.getSecretKey();
    }

    public Map<String, byte[]> encrypt(String stringToEncrypt) throws Exception {
        byte[] dataToEncrypt = stringToEncrypt.getBytes(StandardCharsets.UTF_8);

        SecretKey secretKey = getSecretKey();

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] ivBytes = cipher.getIV();
        byte[] encryptedBytes = cipher.doFinal(dataToEncrypt);

        Map<String, byte[]> map = new HashMap<>();
        map.put(MAP_IV_KEY, ivBytes);
        map.put(MAP_ENCRYPTED_KEY, encryptedBytes);

        return map;
    }

    public byte[] decrypt(Map<String, byte[]> map) throws Exception {
        SecretKey secretKey = getSecretKey();

        byte[] encryptedBytes = map.get(MAP_ENCRYPTED_KEY);
        byte[] ivBytes = map.get(MAP_IV_KEY);

        Cipher cipher = Cipher.getInstance(TRANSFORMATION);

        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, ivBytes);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);

        System.out.println(encryptedBytes.length);
        byte[] decrypted = cipher.doFinal(encryptedBytes);

        System.out.println("Decrypted length: " + decrypted.length);

        return decrypted;
    }
}
