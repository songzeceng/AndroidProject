package com.example.songzeceng.studyofretrofit.utils;

import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;

import java.security.KeyStore;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class KeyUtil {

    private static final String sALIAS = "123";
    // 算法/模式/补码方式
    private static final String sTRANSFORMATION = "AES/GCM/NoPadding";

    private static byte[] sEncryptIv;

    private static void createKey() {
        // 获取Android KeyGenerator的实例
        // 设置使用KeyGenerator的生成的密钥加密算法是AES,在 AndroidKeyStore 中保存密钥/数据
        final KeyGenerator keyGenerator;
        AlgorithmParameterSpec spec = null;
        try {
            keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES,
                    "AndroidKeyStore");
            // 使用KeyGenParameterSpec.Builder 创建KeyGenParameterSpec ,传递给KeyGenerators的init方法
            // KeyGenParameterSpec 是生成的密钥的参数
            // setBlockMode保证了只有指定的block模式下可以加密,解密数据,如果使用其它的block模式,将会被拒绝。
            // 使用了“AES/GCM/NoPadding”变换算法,还需要设置KeyGenParameterSpec的padding类型
            // 创建一个开始和结束时间,有效范围内的密钥对才会生成。
            Calendar start = new GregorianCalendar();
            Calendar end = new GregorianCalendar();
            end.add(Calendar.YEAR, 10); // 往后加十年

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                spec = new KeyGenParameterSpec.Builder(sALIAS,
                        KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                        .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                        .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                        .setCertificateNotBefore(start.getTime())
                        .setCertificateNotAfter(end.getTime())
                        .build();
            }

            keyGenerator.init(spec);
            keyGenerator.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String encryptData(String text) {
        if (isHaveKeyStore()) {
            createKey();
        }

        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry)
                    keyStore.getEntry(sALIAS, null);

            SecretKey secretKey = secretKeyEntry.getSecretKey();

            // KeyGenParameterSpecs中设置的block模式是KeyProperties.BLOCK_MODE_GCM,所以这里只能使用这个模式解密数据。
            Cipher cipher = Cipher.getInstance(sTRANSFORMATION);
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            // ciphers initialization vector (IV)的引用,用于解密
            sEncryptIv = cipher.getIV();
            return Base64.encodeToString(cipher.doFinal(text.getBytes()), Base64.NO_WRAP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String decryptData(String needDecrypt) {
        if (isHaveKeyStore()) {
            createKey();
        }

        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.SecretKeyEntry secretKeyEntry = (KeyStore.SecretKeyEntry) keyStore.getEntry(sALIAS, null);

            SecretKey secretKey = secretKeyEntry.getSecretKey();

            // KeyGenParameterSpecs中设置的block模式是KeyProperties.BLOCK_MODE_GCM,所以这里只能使用这个模式解密数据。
            Cipher cipher = Cipher.getInstance(sTRANSFORMATION);
            // 需要为GCMParameterSpec 指定一个认证标签长度(可以是128、120、112、104、96这个例子中我们能使用最大的128),
            // 并且用到之前的加密过程中用到的IV。
            GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(128, sEncryptIv);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, gcmParameterSpec);
            return new String(cipher.doFinal(Base64.decode(needDecrypt, Base64.NO_WRAP)));

        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static boolean isHaveKeyStore() {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            KeyStore.Entry keyEntry = keyStore.getEntry(sALIAS, null);
            if (keyEntry != null) {
                return false;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
