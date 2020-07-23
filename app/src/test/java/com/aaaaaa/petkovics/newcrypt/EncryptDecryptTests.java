package com.aaaaaa.petkovics.newcrypt;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class EncryptDecryptTests {

    @Test
    public void tests() {
        try {
            Cryptor cryptor = Cryptor.getInstance();
            String decrypted = new String(cryptor.decrypt(cryptor.encrypt("testwordtoencryptthenlaterdecrypt")));

            assertEquals("testwordtoencryptthenlaterdecrypt", decrypted);
        } catch (Exception e) {}

        try {
            Cryptor cryptor = Cryptor.getInstance();
            String decrypted = new String(cryptor.decrypt(cryptor.encrypt("secondword")));

            assertEquals("secondword", decrypted);
        } catch (Exception e) {}

        try {
            Cryptor cryptor = Cryptor.getInstance();
            String decrypted = new String(cryptor.decrypt(cryptor.encrypt("adsfdsdaffdsfadsfds")));

            assertEquals("adsfdsdaffdsfadsfds", decrypted);
        } catch (Exception e) {}

        try {
            Cryptor cryptor = Cryptor.getInstance();
            String decrypted = new String(cryptor.decrypt(cryptor.encrypt("hgfggfghsaddfasdfssd")));

            assertEquals("hgfggfghsaddfasdfssd", decrypted);
        } catch (Exception e) {}
    }

}