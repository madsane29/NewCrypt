package com.aaaaaa.petkovics.newcrypt;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import lombok.Data;

@Data
public class EncryptDecrypt {
    private String ALIAS;
    private byte[] iv;
    private byte[] encryptedBytes;
    private String TRANSFORMATION = "AES/GCM/NoPadding";

    public void encrypt(String password) throws Exception{
        System.out.println("1 - Encrypting: " + password);

        final SecretKey secretKey = getSecretKey();
        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);


        byte[] ivByte = cipher.getIV();
        iv = ivByte;
        System.out.println("2 - iv: " + Base64.encodeToString(iv, Base64.DEFAULT));

        byte[] encryptedBytes2 = cipher.doFinal(password.getBytes(StandardCharsets.UTF_8));
        encryptedBytes = encryptedBytes2;
        System.out.println("3 - encrypted password: " + Base64.encodeToString(encryptedBytes, Base64.DEFAULT));



        //list.add(toObjects(iv));
        //list.add(toObjects(encryptedBytes));


        //return list;
    }

    public void decrypt(List<Byte[]> list) throws Exception {
        //byte[] iv = toPrimitives(list.get(0));
        //byte[] encrypted = toPrimitives(list.get(1));


        System.out.println("4 - Decrypting iv: " + Base64.encodeToString(iv, Base64.DEFAULT));
        System.out.println("5 - Decrypting data: " + Base64.encodeToString(encryptedBytes, Base64.DEFAULT));

        final SecretKey secretKey = getSecretKey();

        final Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        final GCMParameterSpec spec = new GCMParameterSpec(128, iv);

        cipher.init(Cipher.DECRYPT_MODE, secretKey, spec);

        final byte[] decodedData = cipher.doFinal(encryptedBytes);
        final String decrypted = new String(decodedData, StandardCharsets.UTF_8);

        System.out.println("6 - Decrypted data: " + decrypted);

    }


    private byte[] toPrimitives(Byte[] oBytes) {
        byte[] bytes = new byte[oBytes.length];
        for(int i = 0; i < oBytes.length; i++){
            bytes[i] = oBytes[i];
        }
        return bytes;
    }
    private Byte[] toObjects(byte[] bytes) {
        int i = 0;

        Byte[] bytesObjects = new Byte[bytes.length];
        for (Byte b : bytes) {
            bytesObjects[i++] = b;
        }

        return bytesObjects;
    }

    private SecretKey getSecretKey() throws Exception{
        KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
        keyStore.load(null);

        final KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(ALIAS, null);
        return secretKeyEntry.getSecretKey();
    }



    public void createSecretKey() throws Exception {
        final KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");
        final KeyGenParameterSpec keyGenParameterSpec = new KeyGenParameterSpec.Builder("MyKey",
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .build();

        keyGenerator.init(keyGenParameterSpec);
        keyGenerator.generateKey();
    }
}
